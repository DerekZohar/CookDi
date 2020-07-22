package com.example.cookdi.HomeFragment.RecipeAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookdi.HomeFragment.Model.RecipeModel;
import com.example.cookdi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecipeHomeAdapter extends RecyclerView.Adapter<RecipeHomeAdapter.ViewHolder> {
    private Context m_context;
    private ArrayList<RecipeModel> m_recipeList;

    public RecipeHomeAdapter(Context context, ArrayList<RecipeModel> recipeList){
        m_context = context;
        m_recipeList = recipeList;
    }



    @NonNull
    @Override
    public RecipeHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(m_context).inflate(R.layout.home_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHomeAdapter.ViewHolder holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        RecipeModel currentRecipe = m_recipeList.get(position);
        holder.userName.setText(currentRecipe.getUserName());
        holder.recipeName.setText(currentRecipe.getRecipeName());
        holder.recipeTime.setText(currentRecipe.getRecipeTime());

//        holder.foodPortrait.setImageResource(R.color.colorPrimary);
        Picasso.get().setLoggingEnabled(true);
//
        Picasso.get().load(currentRecipe.getFoodPortrait()).error(R.color.colorPrimary).placeholder(R.color.colorGray).resize(200, 180).into(holder.foodPortrait);
        Picasso.get().load(currentRecipe.getUserAvatar()).error(R.color.colorPrimary).placeholder(R.color.colorGray).into(holder.userAvatar);
//        Log.d("aaaaa", currentRecipe.getFoodPortrait());
//        Glide.with(m_context).load(currentRecipe.getUserAvatar()).placeholder(R.drawable.ic_placeholder_background).into(holder.userAvatar);
//        Glide.with(m_context).load("https://disease.sh/assets/img/flags/vn.png").placeholder(R.drawable.ic_placeholder_background).into(holder.foodPortrait);
//        holder.foodPortrait.setImageResource(R.color.colorPrimary);
        holder.recipeRating.setRating((float) currentRecipe.getRecipeRating());
        if(currentRecipe.isRecipeFavorited())
            holder.recipeFavorited.requestFocus();
        if(currentRecipe.isRecipeSaved())
            holder.recipeSaved.requestFocus();

    }

    @Override
    public int getItemCount() {
        return m_recipeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView userAvatar;
        private ImageView foodPortrait;
        private TextView userName;
        private TextView recipeName;
        private TextView recipeTime;
        private RatingBar recipeRating;
        private ImageButton recipeSaved;
        private ImageButton recipeFavorited;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userAvatar = itemView.findViewById(R.id.imgUserAvatarHomeRecipe);
            foodPortrait = itemView.findViewById(R.id.imgFoodPortraitHomeRecipe);
            userName = itemView.findViewById(R.id.txtUsernameHomeRecipe);
            recipeName = itemView.findViewById(R.id.txtFoodNameHomeRecipe);
            recipeTime = itemView.findViewById(R.id.txtTimeHomeRecipe);
            recipeRating = itemView.findViewById(R.id.barRatingFoodHomeRecipe);
            recipeSaved = itemView.findViewById(R.id.btnSaveHomeRecipe);
            recipeFavorited = itemView.findViewById(R.id.btnFavoriteHomeRecipe);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
