package com.example.cookdi.RecipeStep;

import android.widget.ImageView;

import com.example.cookdi.R;
import com.squareup.picasso.Picasso;

import java.time.format.DateTimeFormatter;

public class Step {
    private int number;
    private DateTimeFormatter time;
    private ImageView imgStep;
    private String title;
    private String estimateTime;
    private String tip;
    private String voice;


    public Step(int number, DateTimeFormatter time, ImageView imgStep, String title, String estimateTime, String tip, String voice) {
        this.number = number;
        this.time = time;
        this.imgStep = imgStep;
        this.title = title;
        this.estimateTime = estimateTime;
        this.tip = tip;
        this.voice = voice;
    }


    public int getNumber() {
        return number;
    }

    public DateTimeFormatter getTime() {
        return time;
    }

    public ImageView getImgStep(ImageView imageView) {
        String imgURL  = "https://i.pinimg.com/originals/3b/8a/d2/3b8ad2c7b1be2caf24321c852103598a.jpg";
        Picasso.get().load(imgURL).placeholder(R.mipmap.user).into(imageView);
        return imageView;
    }

    public String getTitle() {
        return title;
    }

    public String getEstimateTime() {
        return estimateTime;
    }

    public String getTip() {
        return tip;
    }

    public String getVoice() {
        return voice;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setTime(DateTimeFormatter time) {
        this.time = time;
    }

    public void setImgStep(ImageView imgStep) {
        this.imgStep = imgStep;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setEstimateTime(String estimateTime) {
        this.estimateTime = estimateTime;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }
}
