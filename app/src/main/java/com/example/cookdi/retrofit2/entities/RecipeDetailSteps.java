package com.example.cookdi.retrofit2.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RecipeDetailSteps {
    @SerializedName("recipe")
    @Expose
    Recipe recipe;

    @SerializedName("steps")
    @Expose
    ArrayList<RecipeStep> steps;

    @SerializedName("chef")
    @Expose
    User chef;

    @SerializedName("ingredients")
    @Expose
    private ArrayList<Ingredient> ingredients;

    @SerializedName("isFavorite")
    @Expose
    private Boolean isFavorite;

    @SerializedName("tags")
    @Expose
    private ArrayList<Tag> tags;


    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public ArrayList<RecipeStep> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<RecipeStep> steps) {
        this.steps = steps;
    }

    public User getChef() {
        return chef;
    }

    public void setChef(User chef) {
        this.chef = chef;
    }


    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }
}
