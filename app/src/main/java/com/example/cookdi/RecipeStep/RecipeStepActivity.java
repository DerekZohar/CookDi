package com.example.cookdi.RecipeStep;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cookdi.R;
import com.example.cookdi.retrofit2.ServiceManager;
import com.example.cookdi.retrofit2.entities.RecipeDetail;
import com.example.cookdi.retrofit2.entities.RecipeDetailSteps;
import com.example.cookdi.retrofit2.entities.RecipeStep;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeStepActivity extends AppCompatActivity {
    RecipeDetailSteps recipeDetailSteps;

    ImageView imageView;
    TextView description;
    TextView esimateTime;
    ImageButton preBtn;
    ImageButton nextBtn;
    Button micro;

    int stepID = 1;
    ArrayList<RecipeStep> listSteps;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        fetchData();

        imageView = (ImageView) findViewById(R.id.imgStep);
        description = findViewById(R.id.stepDescriptionTxt);
        esimateTime = findViewById(R.id.stepEstimateTimeTxt);
        preBtn = findViewById(R.id.previousBtn);
        nextBtn = findViewById(R.id.nextBtn);
        onClickNextBtn();
        onClickPreviousBtn();

//        String imgURL  = "https://i.pinimg.com/originals/3b/8a/d2/3b8ad2c7b1be2caf24321c852103598a.jpg";
//        Picasso.get().load(imgURL).placeholder(R.mipmap.picture_icon_placeholder).into(imageView);


//        countDown(3);

//        System.out.println(recipeDetailSteps.getRecipeId());
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
    private void onClickPreviousBtn(){
        preBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("____________");
                System.out.println(4123);
                if(stepID != 1)
                {

                }
            }
        });
    }
    private void onClickNextBtn(){
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("___________");
                System.out.println("412");
                if(stepID != listSteps.size()){
                    startActivity(new Intent(RecipeStepActivity.this, RecipeStepActivity.class));
                    System.out.println(123);
                }
            }
        });
    }
    private void fetchData(){
        ServiceManager.getInstance().getRecipeService().getRecipeSteps().enqueue(new Callback<RecipeDetailSteps>() {
            @Override
            public void onResponse(Call<RecipeDetailSteps> call, Response<RecipeDetailSteps> response) {
                 recipeDetailSteps = response.body();
                 listSteps = recipeDetailSteps.getSteps();

                setAdapter();
            }

            @Override
            public void onFailure(Call<RecipeDetailSteps> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
    private void setAdapter(){
        Picasso.get().load(recipeDetailSteps.getImageUrl()).placeholder(R.mipmap.picture_icon_placeholder).into(imageView);

//        description.setText(recipeDetailSteps.getSteps().get(0).getRecipe_id());
    }
}
