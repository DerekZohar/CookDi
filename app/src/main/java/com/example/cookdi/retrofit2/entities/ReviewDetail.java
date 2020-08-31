package com.example.cookdi.retrofit2.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ReviewDetail {
    @SerializedName("user_id")
    @Expose
    private Integer user_id;
    @SerializedName("comment_id")
    @Expose
    private Integer comment_id;
    @SerializedName("recipe_id")
    @Expose
    private Integer recipe_id;
    @SerializedName("comment_content")
    @Expose
    private String comment_content;
    @SerializedName("rate")
    @Expose
    private Float rate;
    @SerializedName("comment_time")
    @Expose
    private Date comment_time;

    public Integer getUser_ID() {
        return user_id;
    }

    public void setUser_ID(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getComment_ID() {
        return comment_id;
    }

    public void setComment_ID(Integer comment_id) {
        this.comment_id = comment_id;
    }

    public Integer getRecipe_ID() {
        return recipe_id;
    }

    public void setRecipe_ID(Integer recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getComment_Content() {
        return comment_content;
    }

    public void setComment_Content(String comment_content) {
        this.comment_content = comment_content;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public Date getComment_Time() {
        return comment_time;
    }

    public void setComment_Time(Date comment_time) {
        this.comment_time = comment_time;
    }
}
