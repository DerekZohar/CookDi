package com.example.cookdi.retrofit2.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("user_id")
    @Expose
    private Integer id;
    @SerializedName("user_name")
    @Expose
    private String name;
    @SerializedName("user_age")
    @Expose
    private Integer age;
    @SerializedName("user_gender")
    @Expose
    private Integer gender;
    @SerializedName("user_email")
    @Expose
    private String email;
    @SerializedName("user_avatar_url")
    @Expose
    private String avatar;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }
}
