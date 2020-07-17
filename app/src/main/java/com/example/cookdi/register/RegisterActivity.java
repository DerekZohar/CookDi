package com.example.cookdi.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cookdi.HomeFragment.HomeFragment;
import com.example.cookdi.R;
import com.example.cookdi.config.Config;
import com.example.cookdi.helpers.TextHelper;
import com.example.cookdi.libs.bcrypt.BCrypt;
import com.example.cookdi.login.LoginActivity;
import com.example.cookdi.main.MainActivity;
import com.example.cookdi.retrofit2.ServiceManager;
import com.example.cookdi.retrofit2.entities.User;
import com.example.cookdi.splash.SplashActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText username, password, rePassword;
    Button registerButton;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username_edit_text);
        password = findViewById(R.id.password_edit_text);
        rePassword = findViewById(R.id.retype_password_edit_text);
        registerButton = findViewById(R.id.register_button);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        this.addListener();

        findViewById(R.id.register_view).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyBoard();
                return true;
            }
        });
    }

    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void addListener() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegisterButtonClicked();
            }
        });
    }

    private void onRegisterButtonClicked() {
        if (TextHelper.isTextEmpty(username.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter username", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextHelper.isTextEmpty(password.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextHelper.isTextEmpty(rePassword.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please retype password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.getText().toString().equals(rePassword.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Your retype password not match with your password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();
        ServiceManager.getInstance().getUserService().getUserByName(username.getText().toString()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    Toast.makeText(getApplicationContext(), "Username was existed.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else if (response.code() == 403) {
                    registerUserAccount();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void registerUserAccount() {
        Map<String, Object> params = new HashMap<>();
        params.put("username", username.getText().toString());
        params.put("password", BCrypt.hashpw(password.getText().toString(), BCrypt.gensalt(Config.LOG_ROUND_SALT)));
        
        ServiceManager.getInstance().getUserService().registerAccount(params).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                Toast.makeText(getApplicationContext(), "Register successful", Toast.LENGTH_SHORT).show();
                openHomepage();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                int a = 10;
                progressDialog.dismiss();
            }
        });
    }

    private void openHomepage() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
