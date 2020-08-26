package com.example.cookdi.retrofit2.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipeDetail {
    @SerializedName("recipe")
    @Expose
    private Recipe recipe;
    @SerializedName("isFavorite")
    @Expose
    private Boolean chef;

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Boolean getChef() {
        return chef;
    }

    public void setChef(Boolean chef) {
        this.chef = chef;
    }
}
