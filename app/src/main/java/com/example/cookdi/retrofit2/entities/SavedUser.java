package com.example.cookdi.retrofit2.entities;

public class SavedUser extends User {
    private byte[] image;

    public void setImage(byte[] image){
        image = image;
    }

    public byte[] getImage() {
        return image;
    }
}
