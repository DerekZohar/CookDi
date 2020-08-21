package com.example.cookdi.detail;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cookdi.R;
import com.example.cookdi.detail.IngredientRecyclerView.IngredientModel;
import com.example.cookdi.detail.IngredientRecyclerView.IngredientRecyclerViewAdapter;
import com.example.cookdi.detail.ReviewRecyclerView.ReviewModel;
import com.example.cookdi.detail.ReviewRecyclerView.ReviewRecyclerViewAdapter;
import com.example.cookdi.detail.StepRecyclerView.StepModel;
import com.example.cookdi.detail.StepRecyclerView.StepRecyclerViewAdapter;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private RecyclerView ingredienRecyclerView;
    private RecyclerView stepRecyclerView;
    private RecyclerView reviewRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//        getSupportActionBar().setElevation(0);

        ingredienRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewIngredientsDetailedRecipe);
        stepRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewStepsDetailedRecipe);
        reviewRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewReviewDetailedRecipe);

        final EditText reviewEditText = (EditText) findViewById(R.id.editTextReviewDetailedRecipe);
        reviewEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        reviewEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        LoadData();
    }

    protected void LoadData() {
        String[] ingredients = new String[]{"100g Liêm sỉ", "250g Nhân cách", "100g Tự trọng"};

        ArrayList<IngredientModel> ingredientModels = new ArrayList<IngredientModel>();
        ingredientModels.add(new IngredientModel("100g Liêm sỉ"));
        ingredientModels.add(new IngredientModel("250g Nhân cách"));
        ingredientModels.add(new IngredientModel("150g Tự trọng"));

        ArrayList<StepModel> stepModels = new ArrayList<StepModel>();
        stepModels.add(new StepModel(1, "Cắt"));
        stepModels.add(new StepModel(2, "Xắt"));
        stepModels.add(new StepModel(3, "Xiên"));

        ArrayList<ReviewModel> reviewModels = new ArrayList<ReviewModel>();
        reviewModels.add(new ReviewModel("Ngô Việt Thắng", "Ôi á ớ gì phiên phớt bỏ hút mát nứ phứng bạch bốp bang bi bô phe thang su ca đi giông mê", 4));
        reviewModels.add(new ReviewModel("Nguyễn Hữu Tuấn", "Ôi á ớ gì phiên phớt bỏ hút mát nứ phứng bạch bốp bang bi bô phe thang su ca đi giông mê", 4.5f));
        reviewModels.add(new ReviewModel("Trần Thuận Thành", "Ôi á ớ gì phiên phớt bỏ hút mát nứ phứng bạch bốp bang bi bô phe thang su ca đi giông mê", 5));
        reviewModels.add(new ReviewModel("Nguyễn Hữu Tiến", "Ôi á ớ gì phiên phớt bỏ hút mát nứ phứng bạch bốp bang bi bô phe thang su ca đi giông mê", 4));

        ingredienRecyclerView.setAdapter(new IngredientRecyclerViewAdapter(this, ingredientModels));
        ingredienRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        stepRecyclerView.setAdapter(new StepRecyclerViewAdapter(this, stepModels));
        stepRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        reviewRecyclerView.setAdapter(new ReviewRecyclerViewAdapter(this, reviewModels));
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}