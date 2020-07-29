package com.example.cookdi.splash;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookdi.RecipeStep.RecipeStep;
import com.example.cookdi.login.LoginActivity;
import com.example.cookdi.main.MainActivity;


public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SystemClock.sleep(2000);
        startActivity(new Intent(SplashActivity.this, RecipeStep.class));
//        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

}
