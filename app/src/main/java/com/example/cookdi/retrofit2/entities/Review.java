package com.example.cookdi.retrofit2.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {
    @SerializedName("comment")
    @Expose
    private ReviewDetail comment;

    @SerializedName("commentator")
    @Expose
    private Reviewer commentator;

    public ReviewDetail getComment() {
        return comment;
    }

    public void setComment(ReviewDetail comment) {
        this.comment = comment;
    }

    public Reviewer getCommentator() {
        return commentator;
    }

    public void setCommentator(Reviewer commentator) {
        this.commentator = commentator;
    }
}
