package com.example.cookdi.search;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cookdi.R;
import com.example.cookdi.main.MainActivity;
import com.veinhorn.tagview.TagView;

public class SearchActivity extends AppCompatActivity {
    TextView typeTextView;
    TextView ingredientTextView;
    EditText searchEditText;
    TagView tagViewVegetable;
    TagView tagViewSoup;
    TagView tagViewSnack;
    TagView tagViewNoodle;
    TagView tagViewChicken;
    TagView tagViewBeef;
    TagView tagViewFish;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        typeTextView = findViewById(R.id.typeTextView);
        typeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeTextView.setClickable(false);
            }
        });

        ingredientTextView = findViewById(R.id.ingredientTextView);
        ingredientTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredientTextView.setClickable(false);
            }
        });

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

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
    }

    public void hideKeyBoard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }
}
