package com.example.cookdi.splash;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookdi.RecipeStep.RecipeStepActivity;
import com.example.cookdi.chat.features.main.ChatActivity;
import com.example.cookdi.Report.ReportActivity;
import com.example.cookdi.edit_recipe.EditRecipe;
import com.example.cookdi.login.LoginActivity;
import com.example.cookdi.main.MainActivity;
import com.example.cookdi.sharepref.SharePref;
import com.example.cookdi.upload.UploadActivity;


public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    Intent intent;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemClock.sleep(2000);
//        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//        startActivity(new Intent(SplashActivity.this, EditRecipe.class));
        EditRecipe.open(getApplicationContext(), 781);
        finish();
    }

    void onFlow() {
        if ((SharePref.getInstance(getApplicationContext()).getUuid() == null)) {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
    }

}
