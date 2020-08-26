package com.example.cookdi.retrofit2.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipeDetail {
    @SerializedName("recipe")
    @Expose
    private Recipe recipe;
    @SerializedName("chef")
    @Expose
    private User chef;

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public User getChef() {
        return chef;
    }

    public void setChef(User chef) {
        this.chef = chef;
    }
}
