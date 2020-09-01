package com.example.cookdi.SavedFragment.RecipeAdapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookdi.R;
import com.example.cookdi.SavedFragment.SavedFragment;
import com.example.cookdi.db.RecipeListDBAdapter;
import com.example.cookdi.db.UserListDBAdapter;
import com.example.cookdi.detail.DetailActivity;
import com.example.cookdi.detail.DetailSavedActivity;
import com.example.cookdi.helpers.TextHelper;
import com.example.cookdi.retrofit2.entities.Recipe;
import com.example.cookdi.retrofit2.entities.RecipeDetail;
import com.example.cookdi.retrofit2.entities.SavedRecipe;
import com.example.cookdi.retrofit2.entities.SavedUser;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecipeSavedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_ITEM_LOADING = 1;
    private final int ITEMS_LIMIT = 10;

    private Context m_context;
    private List<RecipeDetail> m_recipeList;

    public RecipeSavedAdapter(Context context, List<RecipeDetail> recipeList){
        m_context = context;
        m_recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(m_context).inflate(R.layout.saved_recipe, parent, false);
            return new com.example.cookdi.SavedFragment.RecipeAdapter.RecipeSavedAdapter.ItemViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(m_context).inflate(R.layout.item_loading, parent, false);
            return new com.example.cookdi.SavedFragment.RecipeAdapter.RecipeSavedAdapter.LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        if(viewHolder instanceof com.example.cookdi.SavedFragment.RecipeAdapter.RecipeSavedAdapter.ItemViewHolder){
            com.example.cookdi.SavedFragment.RecipeAdapter.RecipeSavedAdapter.ItemViewHolder holder = (com.example.cookdi.SavedFragment.RecipeAdapter.RecipeSavedAdapter.ItemViewHolder) viewHolder;

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (RecipeListDBAdapter.isRecipeSaved(m_recipeList.get(position).getRecipe().getRecipeId())) {
                        Intent intent = new Intent(view.getContext(), DetailSavedActivity.class);
                        intent.putExtra("recipe_id", m_recipeList.get(position).getRecipe().getRecipeId());
                        view.getContext().startActivity(intent);
                    }
                    else {

                    }
                }
            });

            final RecipeDetail currentRecipe = m_recipeList.get(position);
            holder.userName.setText(currentRecipe.getChef().getName());
            holder.recipeName.setText(currentRecipe.getRecipe().getRecipeName());
            holder.recipeTime.setText(convertTime(currentRecipe.getRecipe().getTime()));
            holder.recipeRating.setRating((float) currentRecipe.getRecipe().getRating());

//            Picasso.get().load(currentRecipe.getRecipe().getImageUrl()).error(R.drawable.ic_error).placeholder(R.drawable.ic_placeholder).into(holder.foodPortrait);
//            Picasso.get().load(currentRecipe.getChef().getAvatar()).error(R.drawable.ic_error).placeholder(R.drawable.ic_placeholder).into(holder.userAvatar);

            SavedRecipe savedRecipe = (SavedRecipe) currentRecipe.getRecipe();
            SavedUser savedUser = (SavedUser) currentRecipe.getChef();

            if(savedRecipe.getImage() != null){
                InputStream inputStream  = new ByteArrayInputStream(savedRecipe.getImage());
                Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
                holder.foodPortrait.setImageBitmap(bitmap);
            }
            else
                holder.foodPortrait.setImageResource(R.drawable.ic_error);

            if(savedUser.getImage() != null){
                InputStream inputStream = new ByteArrayInputStream(savedUser.getImage());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                holder.userAvatar.setImageBitmap(bitmap);
            }
            else
                holder.userAvatar.setImageResource(R.drawable.ic_error);

            holder.recipeRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                            m_recipeList.remove(currentRecipe);
                            notifyDataSetChanged();
//                            UserListDBAdapter.insertUser(currentRecipe.getChef());
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int which) {}});

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
        else if(viewHolder instanceof com.example.cookdi.SavedFragment.RecipeAdapter.RecipeSavedAdapter.LoadingViewHolder){
            com.example.cookdi.SavedFragment.RecipeAdapter.RecipeSavedAdapter.LoadingViewHolder holder = (com.example.cookdi.SavedFragment.RecipeAdapter.RecipeSavedAdapter.LoadingViewHolder) viewHolder;
        }
    }

    String convertTime(int sec) {
        long min = sec / 60;
        return min + "mins";
    }

    @Override
    public int getItemCount() {
//        return m_recipeList == null ? 0 : m_recipeList.size() > ITEMS_LIMIT ? ITEMS_LIMIT : m_recipeList.size();
        return m_recipeList == null ? 0 : m_recipeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return m_recipeList.get(position) == null ? VIEW_TYPE_ITEM_LOADING : VIEW_TYPE_ITEM;
    }


    public int getItemLimit(){
        return this.ITEMS_LIMIT;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView userAvatar;
        private ImageView foodPortrait;
        private TextView userName;
        private TextView recipeName;
        private TextView recipeTime;
        private RatingBar recipeRating;
        private ImageButton recipeRemove;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.imgUserAvatarSavedRecipe);
            foodPortrait = itemView.findViewById(R.id.imgFoodPortraitSavedRecipe);
            userName = itemView.findViewById(R.id.txtUsernameSavedRecipe);
            recipeName = itemView.findViewById(R.id.txtFoodNameSavedRecipe);
            recipeTime = itemView.findViewById(R.id.txtTimeSavedRecipe);
            recipeRating = itemView.findViewById(R.id.barRatingFoodSavedRecipe);
            recipeRemove = itemView.findViewById(R.id.btnRemoveSavedRecipe);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder{

        private ProgressBar prbarItemLoading;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            prbarItemLoading = itemView.findViewById(R.id.prbarItemLoading);
        }
    }

    private void initButtonAction(){

    }

}

