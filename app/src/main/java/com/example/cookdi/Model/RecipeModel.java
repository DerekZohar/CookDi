package com.example.cookdi.Model;

public class RecipeModel {
    private int recipeId;
    private int userId;
    private float recipeRating;
    private boolean recipeSaved;
    private boolean recipeFavorited;
    private String userAvatar;
    private String foodPortrait;
    private String userName;
    private String recipeName;
    private String recipeTime;

    public RecipeModel(int recipeId, int userId, float recipeRating, boolean recipeSaved, boolean recipeFavorited, String userAvatar, String foodPortrait, String userName, String recipeName, String recipeTime) {
        this.recipeId = recipeId;
        this.userId = userId;
        this.recipeRating = recipeRating;
        this.recipeSaved = recipeSaved;
        this.recipeFavorited = recipeFavorited;
        this.userAvatar = userAvatar;
        this.foodPortrait = foodPortrait;
        this.userName = userName;
        this.recipeName = recipeName;
        this.recipeTime = recipeTime;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public float getRecipeRating() {
        return recipeRating;
    }

    public void setRecipeRating(float recipeRating) {
        this.recipeRating = recipeRating;
    }

    public boolean isRecipeSaved() {
        return recipeSaved;
    }

    public void setRecipeSaved(boolean recipeSaved) {
        this.recipeSaved = recipeSaved;
    }

    public boolean isRecipeFavorited() {
        return recipeFavorited;
    }

    public void setRecipeFavorited(boolean recipeFavorited) {
        this.recipeFavorited = recipeFavorited;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getFoodPortrait() {
        return foodPortrait;
    }

    public void setFoodPortrait(String foodPortrait) {
        this.foodPortrait = foodPortrait;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeTime() {
        return recipeTime;
    }

    public void setRecipeTime(String recipeTime) {
        this.recipeTime = recipeTime;
    }
}
