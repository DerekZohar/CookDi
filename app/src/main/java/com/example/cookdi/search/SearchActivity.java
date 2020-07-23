package com.example.cookdi.search;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cookdi.R;
import com.veinhorn.tagview.TagView;

public class SearchActivity extends AppCompatActivity {
    EditText searchEditText;
    TagView tagViewVegetable;
    TagView tagViewSoup;
    TagView tagViewSnack;
    TagView tagViewNoodle;
    TagView tagViewChicken;
    TagView tagViewBeef;
    TagView tagViewFish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchEditText = findViewById(R.id.editTextSearchView);
        tagViewVegetable = findViewById(R.id.tagViewVegetable);
        tagViewVegetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEditText.setText(tagViewVegetable.getText());
            }
        });
        tagViewSoup = findViewById(R.id.tagViewSoup);
        tagViewSoup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEditText.setText(tagViewSoup.getText());
            }
        });
        tagViewSnack = findViewById(R.id.tagViewSnack);
        tagViewSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEditText.setText(tagViewSnack.getText());
            }
        });
        tagViewNoodle = findViewById(R.id.tagViewNoodle);
        tagViewNoodle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEditText.setText(tagViewNoodle.getText());
            }
        });
        tagViewChicken = findViewById(R.id.tagViewChicken);
        tagViewChicken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEditText.setText(tagViewChicken.getText());
            }
        });
        tagViewBeef = findViewById(R.id.tagViewBeef);
        tagViewBeef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEditText.setText(tagViewBeef.getText());
            }
        });
        tagViewFish = findViewById(R.id.tagViewFish);
        tagViewFish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEditText.setText(tagViewFish.getText());
            }
        });

        findViewById(R.id.search).setOnTouchListener(new View.OnTouchListener() {
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
}
