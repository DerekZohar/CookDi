package com.example.cookdi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cookdi.retrofit2.ServiceManager;
import com.example.cookdi.retrofit2.entities.User;

import javax.xml.transform.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.testText);

        ServiceManager.getInstance().getUserService().getUserInformation().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.e("Trung", "Inside");
                User result = response.body();
                if (response.isSuccessful()) {
                    textView.setText(result.name);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Trung", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}