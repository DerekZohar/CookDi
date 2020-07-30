package com.example.cookdi.splash;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookdi.login.LoginActivity;
import com.example.cookdi.main.MainActivity;
import com.example.cookdi.retrofit2.ServiceManager;
import com.example.cookdi.retrofit2.entities.Tag;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //demo call get list tag
        ServiceManager.getInstance().getTagService().getAllTag().enqueue(new Callback<List<Tag>>() {
            @Override
            public void onResponse(Call<List<Tag>> call, Response<List<Tag>> response) {
                List<Tag> tags = response.body();
                int a = 10;
                Log.e("eorr",tags.get(0).name);
            }

            @Override
            public void onFailure(Call<List<Tag>> call, Throwable t) {
                Log.e("eorr",t.getMessage());

            }
        });



        SystemClock.sleep(2000);
        //startActivity(new Intent(SplashActivity.this, MainActivity.class));
//        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        //finish();
    }

}
