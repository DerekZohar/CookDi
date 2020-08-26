package com.example.cookdi.FavoriteFragment.RecipeAdapter;

import android.content.Context;
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

import com.example.cookdi.Model.RecipeModel;
import com.example.cookdi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecipeFavoriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_ITEM_LOADING = 1;
    private final int ITEMS_LIMIT = 10;

    private Context m_context;
    private ArrayList<RecipeModel> m_recipeList;

    public RecipeFavoriteAdapter(Context context, ArrayList<RecipeModel> recipeList){
        m_context = context;
        m_recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(m_context).inflate(R.layout.favorite_recipe, parent, false);
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

            RecipeModel currentRecipe = m_recipeList.get(position);
            holder.userName.setText(currentRecipe.getUserName());
            holder.recipeName.setText(currentRecipe.getRecipeName());
            holder.recipeTime.setText(currentRecipe.getRecipeTime());

//        Picasso.get().setLoggingEnabled(true);
            Picasso.get().load(currentRecipe.getFoodPortrait()).error(R.drawable.ic_error).placeholder(R.drawable.ic_placeholder).into(holder.foodPortrait);
            Picasso.get().load(currentRecipe.getUserAvatar()).error(R.drawable.ic_error).placeholder(R.drawable.ic_placeholder).into(holder.userAvatar);

            //
            holder.recipeRating.setRating((float) currentRecipe.getRecipeRating());

            //
//            if(currentRecipe.isRecipeFavorited())
//                holder.recipeFavorited.setBackgroundResource(R.drawable.ic_favorited);
//            else
//                holder.recipeFavorited.setBackgroundResource(R.drawable.ic_favorite);
//            if(currentRecipe.isRecipeSaved())
//                holder.recipeSaved.setBackgroundResource(R.drawable.ic_bookmarked);
//            else
//                holder.recipeSaved.setBackgroundResource(R.drawable.ic_bookmark);

        }
        else if(viewHolder instanceof LoadingViewHolder){
            LoadingViewHolder holder = (LoadingViewHolder) viewHolder;
        }
    }

    @Override
    public int getItemCount() {
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

            userAvatar = itemView.findViewById(R.id.imgUserAvatarFavoriteRecipe);
            foodPortrait = itemView.findViewById(R.id.imgFoodPortraitFavoriteRecipe);
            userName = itemView.findViewById(R.id.txtUsernameFavoriteRecipe);
            recipeName = itemView.findViewById(R.id.txtFoodNameFavoriteRecipe);
            recipeTime = itemView.findViewById(R.id.txtTimeFavoriteRecipe);
            recipeRating = itemView.findViewById(R.id.barRatingFoodFavoriteRecipe);
            recipeSaved = itemView.findViewById(R.id.btnSaveFavoriteRecipe);
            recipeFavorited = itemView.findViewById(R.id.btnFavoriteFavoriteRecipe);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder{

        private ProgressBar prbarItemLoading;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            prbarItemLoading = itemView.findViewById(R.id.prbarItemLoading);
        }
    }

}
