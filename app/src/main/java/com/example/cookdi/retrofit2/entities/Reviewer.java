package com.example.cookdi.retrofit2.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reviewer {
    @SerializedName("user_id")
    @Expose
    private Integer user_id;

    @SerializedName("user_name")
    @Expose
    private String user_name;

    @SerializedName("user_email")
    @Expose
    private String user_email;

    @SerializedName("user_gender")
    @Expose
    private String user_gender;

    @SerializedName("user_age")
    @Expose
    private Integer user_age;

    @SerializedName("user_avatar_url")
    @Expose
    private String user_avatar_url;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(String user_gender) {
        this.user_gender = user_gender;
    }

    public Integer getUser_age() {
        return user_age;
    }

    public void setUser_age(Integer user_age) {
        this.user_age = user_age;
    }

    public String getUser_avatar_url() {
        return user_avatar_url;
    }

    public void setUser_avatar_url(String user_avatar_url) {
        this.user_avatar_url = user_avatar_url;
    }
}
