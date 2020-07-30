package com.example.cookdi.retrofit2.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tag {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("title")
    @Expose
    public String name;

    public Tag(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Tag() {

    }
}
