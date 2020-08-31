package com.example.cookdi.ManageRecipe.RecipeAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.cookdi.retrofit2.entities.RecipeDetail;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecipeHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_ITEM_LOADING = 1;
    private final int ITEMS_LIMIT = 10;

    private Context m_context;
    private List<RecipeDetail> m_recipeList;

    public RecipeHomeAdapter(Context context, List<RecipeDetail> recipeList){
        m_context = context;
        m_recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(m_context).inflate(R.layout.home_recipe, parent, false);
            return new ItemViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(m_context).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if(viewHolder instanceof ItemViewHolder){
            ItemViewHolder holder = (ItemViewHolder) viewHolder;

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            final RecipeDetail currentRecipe = m_recipeList.get(position);
            holder.userName.setText(currentRecipe.getChef().getName());
            holder.recipeName.setText(currentRecipe.getRecipe().getRecipeName());
            holder.recipeTime.setText(convertTime(currentRecipe.getRecipe().getTime()));

//        Picasso.get().setLoggingEnabled(true);
            Picasso.get().load(currentRecipe.getRecipe().getImageUrl()).error(R.drawable.ic_error).placeholder(R.drawable.ic_placeholder).into(holder.foodPortrait);
            Picasso.get().load(currentRecipe.getChef().getAvatar()).error(R.drawable.ic_error).placeholder(R.drawable.ic_placeholder).into(holder.userAvatar);

            //
            holder.recipeRating.setRating((float) currentRecipe.getRecipe().getRating());

            //
//            initButtonAction();
//            if(currentRecipe.isRecipeFavorited())
//                holder.recipeFavorited.setPressed(true);
//            else
//                holder.recipeFavorited.setPressed(false);
//            if(currentRecipe.isRecipeSaved())
//                holder.recipeSaved.setPressed(true);
//            else
//                holder.recipeSaved.setPressed(true);

            //
            holder.recipeSaved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
                    builder.setCancelable(true);
                    builder.setTitle("Save");
                    builder.setMessage("Saving \"" +currentRecipe.getRecipe().getRecipeName()+"\" recipe to your device?");
                    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            currentRecipe.setRecipeSaved(true);
                            //... db update
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int which) {}});

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            holder.recipeFavorited.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        currentRecipe.setRecipeFavorited(true);
                        //... db update
                    }
            });

        }
        else if(viewHolder instanceof LoadingViewHolder){
            LoadingViewHolder holder = (LoadingViewHolder) viewHolder;
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
