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
import com.example.cookdi.helpers.TextHelper;
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

    ImageView imgStep;
    TextView description;
    TextView estimateTime;
    TextView stepNumber;
    ImageButton preBtn;
    ImageButton nextBtn;
    Button micro;

    int stepID = 1;
    ArrayList<RecipeStep> listSteps;

    String FORMAT_TIME = "%02d:%02d:%02d";
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        fetchData();

        imgStep = (ImageView) findViewById(R.id.imgStep);
        description = findViewById(R.id.stepDescriptionTxt);
        stepNumber = findViewById(R.id.stepNumber);
        estimateTime = findViewById(R.id.stepEstimateTimeTxt);
        preBtn = findViewById(R.id.previousBtn);
        nextBtn = findViewById(R.id.nextBtn);
        onClickNextBtn();
        onClickPreviousBtn();

    }

    //time: second
    public void countDown(int time){
        final TextView textView = (TextView) findViewById(R.id.countDownTime);
        new CountDownTimer(time*1000, 1000) { // adjust the milli seconds here

            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            public void onTick(long millisUntilFinished) {

                textView.setText(""+String.format(FORMAT_TIME,
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
                if(stepID != 1)
                {
                    stepID--;
                    startActivity(new Intent(RecipeStepActivity.this, RecipeStepActivity.class));
                }
            }
        });
    }
    private void onClickNextBtn(){
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stepID != listSteps.size()){
                    stepID++;
                    startActivity(new Intent(RecipeStepActivity.this, RecipeStepActivity.class));
                }
            }
        });
    }
    private void fetchData(){
        ServiceManager.getInstance().getRecipeService().getRecipeSteps().enqueue(new Callback<RecipeDetailSteps>() {
            @Override
            public void onResponse(Call<RecipeDetailSteps> call, Response<RecipeDetailSteps> response) {
                     recipeDetailSteps = response.body();


                setAdapter();
            }

            @Override
            public void onFailure(Call<RecipeDetailSteps> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
    @SuppressLint("SetTextI18n")
    private void setAdapter(){
        listSteps = recipeDetailSteps.getSteps();

        String imgUrl = recipeDetailSteps.getSteps().get(stepID - 1).getStep_image_url();
        if(TextHelper.isTextEmpty(imgUrl) & TextHelper.isURL(imgUrl)){
            Picasso.get().load(imgUrl)
                    .placeholder(R.mipmap.picture_icon_placeholder)
                    .error(R.mipmap.picture_icon_placeholder)
                    .into(imgStep);
        }


        stepNumber.setText("Step: " +  "1/" + listSteps.size());
        countDown(recipeDetailSteps.getRecipe().getTime());

        description.setText(recipeDetailSteps.getRecipe().getDescription());
        int estTime = recipeDetailSteps.getRecipe().getTime()*1000;
        estimateTime.setText(""+String.format(FORMAT_TIME,
                TimeUnit.MILLISECONDS.toHours(estTime),
                TimeUnit.MILLISECONDS.toMinutes(estTime) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(estTime)),
                TimeUnit.MILLISECONDS.toSeconds(estTime) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(estTime))));
    }
}
