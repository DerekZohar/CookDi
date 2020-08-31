package com.example.cookdi.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cookdi.R;
import com.example.cookdi.helpers.TextHelper;
import com.example.cookdi.retrofit2.ServiceManager;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendMailActivity extends AppCompatActivity {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    TextView email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_forgot_password);

       email = findViewById(R.id.email_edit_text);

        Button button = findViewById(R.id.next_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextHelper.isTextEmpty(email.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Please enter a your email!", Toast.LENGTH_SHORT).show();

                }
                else{
                    if(validate(email.getText().toString())){
                        OtpMailActivity.open(SendMailActivity.this, email.getText().toString());
                        sendMail();


                        startActivity(new Intent(SendMailActivity.this, OtpMailActivity.class));
                    }else {
                        Toast.makeText(getApplicationContext(), "Please enter right format!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



    }
    private void sendMail(){
        Map<String, Object> params = new HashMap<>();
        params.put("email", email.getText().toString());
        ServiceManager.getInstance().getUserService().forgotPassword(params).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {

            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}
