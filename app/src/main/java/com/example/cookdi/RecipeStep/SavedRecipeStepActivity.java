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
import com.example.cookdi.config.Config;
import com.example.cookdi.helpers.TextHelper;
import com.example.cookdi.retrofit2.ServiceManager;
import com.example.cookdi.retrofit2.entities.RecipeDetail;
import com.example.cookdi.retrofit2.entities.RecipeDetailSteps;
import com.example.cookdi.retrofit2.entities.RecipeStep;
import com.example.cookdi.sharepref.SharePref;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedRecipeStepActivity extends AppCompatActivity {
    RecipeDetailSteps recipeDetailSteps;

    ImageView imgStep;
    TextView description;
    TextView estimateTime;
    TextView stepNumber;
    ImageButton preBtn;
    ImageButton nextBtn;
    Button micro;
    CountDownTimer countDownTimer;
    ArrayList<RecipeStep> listSteps;
    int RecipeID;
    String FORMAT_TIME = "%02d:%02d:%02d";
    int stepID = 1;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        RecipeID = getIntent().getIntExtra("recipe_id", 0);
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


    // time: second
    public void countDown(int time) {
        final TextView textView = (TextView) findViewById(R.id.countDownTime);
        countDownTimer = new CountDownTimer(time * 1000, 1000) { // adjust the milli seconds here

            @SuppressLint({ "DefaultLocale", "SetTextI18n" })
            public void onTick(long millisUntilFinished) {

                textView.setText("" + String.format(FORMAT_TIME, TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {

            }
        }.start();
    }

    private void onClickPreviousBtn() {
        preBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stepID != 1) {
                    stepID = stepID - 1;
                    countDownTimer.cancel();
                    // startActivity(new Intent(RecipeStepActivity.this, RecipeStepActivity.class));
                    setAdapter();
                }
            }
        });
    }

    private void onClickNextBtn() {
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stepID != listSteps.size()) {
                    stepID = stepID + 1;
                    countDownTimer.cancel();
                    // startActivity(new Intent(RecipeStepActivity.this, RecipeStepActivity.class));
                    setAdapter();
                }
            }
        });
    }

    private void fetchData() {
        int uuid = Integer.parseInt(SharePref.getInstance(getApplicationContext()).getUuid());
        ServiceManager.getInstance().getRecipeService().getRecipeSteps(RecipeID, uuid)
                .enqueue(new Callback<RecipeDetailSteps>() {
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
    private void setAdapter() {
        listSteps = recipeDetailSteps.getSteps();
        // System.out.println("__________stepID");
        // System.out.println(Config.stepID);
        // System.out.println("__________");
        // System.out.println(listSteps.size());

        String imgUrl = recipeDetailSteps.getSteps().get(stepID - 1).getStep_image_url();
        if (TextHelper.isTextEmpty(imgUrl) & TextHelper.isURL(imgUrl)) {
            Picasso.get().load(imgUrl).placeholder(R.mipmap.picture_icon_placeholder)
                    .error(R.mipmap.picture_icon_placeholder).into(imgStep);
        }

        stepNumber.setText("Step: " + stepID + "/" + listSteps.size());
        countDown(recipeDetailSteps.getRecipe().getTime());

        description.setText(recipeDetailSteps.getSteps().get(stepID - 1).getStep_description());
        int estTime = recipeDetailSteps.getRecipe().getTime() * 1000;
        estimateTime.setText("" + String.format(FORMAT_TIME, TimeUnit.MILLISECONDS.toHours(estTime),
                TimeUnit.MILLISECONDS.toMinutes(estTime)
                        - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(estTime)),
                TimeUnit.MILLISECONDS.toSeconds(estTime)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(estTime))));
    }
}
