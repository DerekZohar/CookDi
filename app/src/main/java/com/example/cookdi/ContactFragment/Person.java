package com.example.cookdi.ContactFragment;

import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.example.cookdi.R;
public class Person {
    private String fullName;
    private String messenger;
    private String imageName;

    public Person(String fullName, String messenger, String imageName){
        this.fullName=fullName;
        this.messenger=messenger;
        this.imageName = imageName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getFullName() {
        return fullName;
    }

    public String getMessenger() {return ellipsize(messenger, 20);}
    public void setMessenger(String messenger) { this.messenger = messenger; }

    public ImageView getImageView(ImageView imageView) {
        String imgURL  = "abc";
        Picasso.get().load(imgURL).placeholder(R.mipmap.user).into(imageView);
        return imageView;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    String ellipsize(String input, int maxLength) {
        if (input == null || input.length() < maxLength) {
            return input;
        }
        return input.substring(0, maxLength) + "...";
    }
}
