package com.example.cookdi.RecipeStep;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cookdi.R;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

public class RecipeStep extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        ImageView imageView = (ImageView) findViewById(R.id.imgStep);
        String imgURL  = "https://i.pinimg.com/originals/3b/8a/d2/3b8ad2c7b1be2caf24321c852103598a.jpg";
        Picasso.get().load(imgURL).placeholder(R.mipmap.picture_icon_placeholder).into(imageView);

        countDown(3);
    }

    //time: second
    public void countDown(int time){
        final TextView textView = (TextView) findViewById(R.id.countDownTime);
        new CountDownTimer(time*1000, 1000) { // adjust the milli seconds here

            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            public void onTick(long millisUntilFinished) {

                textView.setText(""+String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                
            }
        }.start();
    }
}
