package com.example.cookdi.retrofit2.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ingredient {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("ingredient")
    @Expose
    private String ingredient;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
