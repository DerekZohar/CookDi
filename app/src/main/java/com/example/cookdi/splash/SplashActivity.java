package com.example.cookdi.splash;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookdi.db.DatabaseManager;

import com.example.cookdi.helpers.TextHelper;
import com.example.cookdi.login.LoginActivity;
import com.example.cookdi.main.MainActivity;
import com.example.cookdi.sharepref.SharePref;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private DatabaseManager mDatabaseManager;
    Intent intent;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAppDB();
        SystemClock.sleep(2000);
        onFlow();
        finish();
    }

    void onFlow() {
        if (TextHelper.isTextEmpty(SharePref.getInstance(getApplicationContext()).getUuid())) {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
    }

    void initAppDB(){
        mDatabaseManager = DatabaseManager.newInstance(getApplicationContext());
//        mDatabaseManager.resetDB();
        Log.d("CREATE DATABASE", "Done");
    }

}
