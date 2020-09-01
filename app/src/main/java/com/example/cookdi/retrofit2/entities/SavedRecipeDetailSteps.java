package com.example.cookdi.retrofit2.entities;

import java.util.ArrayList;

public class SavedRecipeDetailSteps extends RecipeDetailSteps {
    private ArrayList<SavedRecipeStep> mSavedRecipeSteps;

    public void setSavedRecipeSteps(ArrayList<SavedRecipeStep> savedRecipeSteps){
        mSavedRecipeSteps = savedRecipeSteps;
    }

    public ArrayList<SavedRecipeStep> getSavedRecipeSteps(){
        return mSavedRecipeSteps;
    }
}
