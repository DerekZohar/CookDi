package com.example.cookdi.retrofit2.entities;

public class SavedRecipe extends Recipe {
    private byte[] image;
    public void setImage(byte[] img){
        image = img;
    }
    public byte[] getImage(){
        return image;
    }
}
