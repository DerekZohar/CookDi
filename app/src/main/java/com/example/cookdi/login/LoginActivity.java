package com.example.cookdi.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookdi.R;
import com.example.cookdi.chat.ioSocketConnector.IOSocketConnector;
import com.example.cookdi.config.Config;
import com.example.cookdi.helpers.TextHelper;
import com.example.cookdi.main.MainActivity;
import com.example.cookdi.register.RegisterActivity;
import com.example.cookdi.retrofit2.ServiceManager;
import com.example.cookdi.sharepref.SharePref;

import java.util.HashMap;
import java.util.Map;

import at.favre.lib.crypto.bcrypt.BCrypt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    Button loginButton;
    ProgressDialog progressDialog;
    TextView forgotPasswordTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username_edit_text);
        password = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);
        forgotPasswordTxt = findViewById(R.id.forgot_password_text_view);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginButtonClicked();
            }
        });

        findViewById(R.id.login_view).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                hideKeyBoard();
                return true;
            }
        });
        forgotPasswordTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SendMailActivity.class));
            }
        });
    }

    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    public void onCreateAccountClick(View v) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void onLoginButtonClicked() {
        if (TextHelper.isTextEmpty(username.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter username", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextHelper.isTextEmpty(password.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("username", username.getText().toString());
        progressDialog.show();
        ServiceManager.getInstance().getUserService().authentication(params).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                BCrypt.Result result = BCrypt.verifyer().verify(password.getText().toString().toCharArray(),response.body().get("pass") );
                if (result.verified) {
                    SharePref.getInstance(getApplicationContext()).setUuid(response.body().get("id").toString());
                    openHomepage();
                } else {
                    Toast.makeText(getApplicationContext(), "Username or password incorrect", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void openHomepage() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
