package com.example.cookdi.detail.ReviewRecyclerView;

public class ReviewModel {
    private String context;
    private String username;
    private float rating;

    public ReviewModel(String username, String context, float rating) {
        this.context = context;
        this.username = username;
        this.rating = rating;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getUsername() {
        return username;
    }

    public void setName(String username) {
        this.username = username;
    }

    public float getRating() { return rating; }

    public void setRating(float rating) { this.rating = rating; }
}
