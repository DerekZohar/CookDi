package com.example.cookdi.RecipeStep;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cookdi.R;
import com.squareup.picasso.Picasso;

public class RecipeStep extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        ImageView imageView = (ImageView) findViewById(R.id.imgStep);
        String imgURL  = "https://i.pinimg.com/originals/3b/8a/d2/3b8ad2c7b1be2caf24321c852103598a.jpg";
        Picasso.get().load(imgURL).placeholder(R.mipmap.user).into(imageView);
    }
}
