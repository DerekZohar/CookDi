package com.example.cookdi.login;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.example.cookdi.R;
import com.example.cookdi.retrofit2.ServiceManager;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpMailActivity extends AppCompatActivity {

    private static String emailOTP;


    public static void open(Context context, String email) {
        Intent intent = new Intent(context, OtpMailActivity.class);
        intent.putExtra("email", email);
        emailOTP = email;
        System.out.println("________________email" + email);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        handleOTP();
    }

    private void handleOTP(){
        final PinEntryEditText pinEntry = (PinEntryEditText) findViewById(R.id.txt_pin_entry);
        if (pinEntry != null) {
            pinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {

                    Map<String, Object> params = new HashMap<>();
                    params.put("email", emailOTP);
                    params.put("code", str.toString());
//                    System.out.println("________________email" + b.getString("email"));

                    ServiceManager.getInstance().getUserService().otpForgotPassword(params).enqueue(new Callback<Map<String, String>>() {
                        @Override
                        public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                            System.out.println("______________" + response.body());
                            if(response.code() == 200){
                                Toast.makeText(OtpMailActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(OtpMailActivity.this, LoginActivity.class));
                            }
                            else
                            {
                                Toast.makeText(OtpMailActivity.this, "Wrong code", Toast.LENGTH_SHORT).show();
                                pinEntry.setText(null);
                            }

                        }

                        @Override
                        public void onFailure(Call<Map<String, String>> call, Throwable t) {
                            Toast.makeText(OtpMailActivity.this, "FAIL", Toast.LENGTH_SHORT).show();
                            t.printStackTrace();
                        }
                    });

                }
            });
        }
    }
//    private String getOTPCode(){
//
//    }
}
