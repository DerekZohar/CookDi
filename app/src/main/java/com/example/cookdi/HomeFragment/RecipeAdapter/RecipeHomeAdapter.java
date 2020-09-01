package com.example.cookdi.HomeFragment.RecipeAdapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookdi.R;
import com.example.cookdi.db.IngredientDBAdapter;
import com.example.cookdi.db.RecipeStepDBAdapter;
import com.example.cookdi.detail.DetailActivity;
import com.example.cookdi.helpers.TextHelper;
import com.example.cookdi.retrofit2.ServiceManager;
import com.example.cookdi.db.RecipeListDBAdapter;
import com.example.cookdi.db.UserListDBAdapter;
import com.example.cookdi.retrofit2.entities.Ingredient;
import com.example.cookdi.retrofit2.entities.RecipeDetail;
import com.example.cookdi.retrofit2.entities.RecipeDetailSteps;
import com.example.cookdi.retrofit2.entities.RecipeStep;
import com.example.cookdi.sharepref.SharePref;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_ITEM_LOADING = 1;
    private final int ITEMS_LIMIT = 10;

    private Context m_context;
    private List<RecipeDetail> m_recipeList;

    public RecipeHomeAdapter(Context context, List<RecipeDetail> recipeList) {
        m_context = context;
        m_recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            Log.d("HOME CONTEXT ", m_context.toString());
            View view = LayoutInflater.from(m_context).inflate(R.layout.home_recipe, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(m_context).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof ItemViewHolder) {
            final ItemViewHolder holder = (ItemViewHolder) viewHolder;

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DetailActivity.class);
                    intent.putExtra("recipe_id", m_recipeList.get(position).getRecipe().getRecipeId());
                    view.getContext().startActivity(intent);
                }
            });

            final RecipeDetail currentRecipe = m_recipeList.get(position);
            holder.userName.setText(currentRecipe.getChef().getName());
            holder.recipeName.setText(currentRecipe.getRecipe().getRecipeName());
            holder.recipeTime.setText(convertTime(currentRecipe.getRecipe().getTime()));

            // Picasso.get().setLoggingEnabled(true);
            if(!TextHelper.isTextEmpty(currentRecipe.getRecipe().getImageUrl()))
                Picasso.get().load(currentRecipe.getRecipe().getImageUrl()).error(R.drawable.ic_error)
                    .placeholder(R.drawable.ic_placeholder).into(holder.foodPortrait);
            if(!TextHelper.isTextEmpty(currentRecipe.getChef().getAvatar()))
                Picasso.get().load(currentRecipe.getChef().getAvatar()).error(R.drawable.ic_error)
                    .placeholder(R.drawable.ic_placeholder).into(holder.userAvatar);

            //
            holder.recipeRating.setRating((float) currentRecipe.getRecipe().getRating());

            //
            if(RecipeListDBAdapter.isRecipeSaved(currentRecipe.getRecipe().getRecipeId()))
                holder.recipeSaved.setBackgroundResource(R.drawable.ic_bookmarked);
            else {
                holder.recipeSaved.setBackgroundResource(R.drawable.ic_bookmark);
            }
            holder.recipeSaved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (RecipeListDBAdapter.isRecipeSaved(currentRecipe.getRecipe().getRecipeId())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
                        builder.setCancelable(true);
                        builder.setTitle("Delete");
                        builder.setMessage("Delete \"" +currentRecipe.getRecipe().getRecipeName()+"\" from device?");
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //db sqlite update
                                RecipeListDBAdapter.deleteById(currentRecipe.getRecipe().getRecipeId());
                                notifyDataSetChanged();
                                holder.recipeSaved.setBackgroundResource(R.drawable.ic_bookmark);
                            }
                        });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int which) {}});

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
                        builder.setCancelable(true);
                        builder.setTitle("Save");
                        builder.setMessage(
                                "Saving \"" + currentRecipe.getRecipe().getRecipeName() + "\" recipe to your device?");
                        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                // button color changed
                                holder.recipeSaved.setBackgroundResource(R.drawable.ic_bookmarked);

                                // db sqlite update
                                RecipeListDBAdapter.insertRecipe(currentRecipe.getRecipe());
                                UserListDBAdapter.insertUser(currentRecipe.getChef());
                                ServiceManager.getInstance().getRecipeService().getRecipeSteps(currentRecipe.getRecipe().getRecipeId(), currentRecipe.getChef().getId()).enqueue(new Callback<RecipeDetailSteps>() {
                                    @Override
                                    public void onResponse(Call<RecipeDetailSteps> call, Response<RecipeDetailSteps> response) {
                                        ArrayList<RecipeStep> recipeSteps = response.body().getSteps();
                                        RecipeStepDBAdapter.insertRecipeSteps(recipeSteps);

                                        ArrayList<Ingredient> ingredients = response.body().getIngredients();
                                        IngredientDBAdapter.insertIngredients(ingredients, currentRecipe.getRecipe().getRecipeId());
                                    }

                                    @Override
                                    public void onFailure(Call<RecipeDetailSteps> call, Throwable t) {
                                    }
                                });
                            }
                        });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            });

            //

            holder.recipeFavorited.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int user_id = Integer.valueOf(SharePref.getInstance(m_context).getUuid());
                    int recipe_id = currentRecipe.getRecipe().getRecipeId();
                    final Map<String, Object> params = new HashMap<>();
                    params.put("user_id", user_id);
                    params.put("recipe_id", recipe_id);
                    ServiceManager.getInstance().getFavoriteService().addToFavorite(params)
                            .enqueue(new Callback<Map<String, String>>() {
                                @Override
                                public void onResponse(Call<Map<String, String>> call,
                                        Response<Map<String, String>> response) {
                                    if (response.message().equals("OK")) {
                                        holder.recipeFavorited.setBackgroundResource(R.drawable.ic_favorite_red);
                                    } else if (response.message().equals("Not Found")) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
                                        builder.setCancelable(true);
                                        builder.setTitle("Warning");
                                        builder.setMessage(currentRecipe.getRecipe().getRecipeName()
                                                + " Is Already In Your Favorite? Would You Like To Remove It?");
                                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                ServiceManager.getInstance().getFavoriteService().delete(params)
                                                        .enqueue(new Callback<Map<String, String>>() {
                                                            @Override
                                                            public void onResponse(Call<Map<String, String>> call,
                                                                    Response<Map<String, String>> response) {
                                                                holder.recipeFavorited
                                                                        .setBackgroundResource(R.drawable.ic_favorite);
                                                            }

                                                            @Override
                                                            public void onFailure(Call<Map<String, String>> call,
                                                                    Throwable t) {

                                                            }
                                                        });
                                            }
                                        });
                                        builder.setNegativeButton(android.R.string.cancel,
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                    }
                                                });
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                                }
                            });

                }
            });

        } else if (viewHolder instanceof LoadingViewHolder) {
            LoadingViewHolder holder = (LoadingViewHolder) viewHolder;
        }
    }

    String convertTime(int sec) {
        long min = sec / 60;
        return min + "mins";
    }

    @Override
    public int getItemCount() {
        // return m_recipeList == null ? 0 : m_recipeList.size() > ITEMS_LIMIT ?
        // ITEMS_LIMIT : m_recipeList.size();
        return m_recipeList == null ? 0 : m_recipeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return m_recipeList.get(position) == null ? VIEW_TYPE_ITEM_LOADING : VIEW_TYPE_ITEM;
    }

    public int getItemLimit() {
        return this.ITEMS_LIMIT;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView userAvatar;
        private ImageView foodPortrait;
        private TextView userName;
        private TextView recipeName;
        private TextView recipeTime;
        private RatingBar recipeRating;
        private ImageButton recipeSaved;
        private ImageButton recipeFavorited;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.imgUserAvatarHomeRecipe);
            foodPortrait = itemView.findViewById(R.id.imgFoodPortraitHomeRecipe);
            userName = itemView.findViewById(R.id.txtUsernameHomeRecipe);
            recipeName = itemView.findViewById(R.id.txtFoodNameHomeRecipe);
            recipeTime = itemView.findViewById(R.id.txtTimeHomeRecipe);
            recipeRating = itemView.findViewById(R.id.barRatingFoodHomeRecipe);
            recipeSaved = itemView.findViewById(R.id.btnSaveHomeRecipe);
            recipeFavorited = itemView.findViewById(R.id.btnFavoriteHomeRecipe);

        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar prbarItemLoading;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            prbarItemLoading = itemView.findViewById(R.id.prbarItemLoading);
        }
    }

    private void initButtonAction() {

    }
}
