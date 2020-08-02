package com.example.cookdi.chat.features.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cookdi.chat.features.demo.styled.StyledDialogsActivity;
import com.example.cookdi.R;



/*
 * Created by troy379 on 04.04.17.
 */
public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StyledDialogsActivity.open(this);
    }
}
