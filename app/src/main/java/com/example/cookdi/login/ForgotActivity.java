package com.example.cookdi.login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cookdi.R;

public class ForgotActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

//        TextView txtBackToSignIn = findViewById(R.id.backToSignIn);

//
//        txtBackToSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ForgotActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}
