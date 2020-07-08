package com.example.cookdi.retrofit2.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("age")
    @Expose
    private int age;

    public String toString() {
        return String.format("%s is %i year old", this.name, this.age);
    }
}
