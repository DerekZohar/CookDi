package com.example.cookdi.Report;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cookdi.R;
import com.example.cookdi.helpers.TextHelper;
import com.example.cookdi.main.MainActivity;
import com.example.cookdi.search.SearchActivity;
import com.example.cookdi.splash.SplashActivity;

public class ReportActivity extends AppCompatActivity {
    Button submit;
    EditText editText;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        submit = (Button) findViewById(R.id.submit_report_button);
        editText = (EditText) findViewById(R.id.desciptionEditText);
        imageView = (ImageView) findViewById(R.id.imageReport);
        submitEvent();
    }
    void submitEvent(){

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextHelper.isTextEmpty(editText.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Please enter Description", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(ReportActivity.this, ReportActivitySucceed.class));
            }
        });
    }

}
