package com.example.cookdi.search;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookdi.HomeFragment.RecipeAdapter.RecipeHomeAdapter;
import com.example.cookdi.R;
import com.example.cookdi.main.MainActivity;
import com.example.cookdi.retrofit2.ServiceManager;
import com.example.cookdi.retrofit2.entities.RecipeDetail;
import com.example.cookdi.sharepref.SharePref;

import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    EditText searchEditText;
    ImageButton backButton;
    ImageView searchImageView;
    List<RecipeDetail> recipeList;
    RecipeHomeAdapter recyclerViewAdapter;
    RecyclerView recyclerView;
    int page = 0;
    Boolean isItemLoading = false;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.SpotsDialog)
                .build();

        recyclerView = findViewById(R.id.recyclerViewSearch);

        searchEditText = findViewById(R.id.editTextSearchView);
        searchEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        searchEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchImageView = findViewById(R.id.imageViewSearch);
        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.clearFocus();
                hideKeyBoard();
                if (!searchEditText.getText().toString().isEmpty()) {
                    page = 0;
                    LoadData();
                }
            }
        });

        findViewById(R.id.search).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyBoard();
                searchEditText.clearFocus();
                searchEditText.clearAnimation();
                return true;
            }
        });

        findViewById(R.id.linearLayoutResultSearch).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyBoard();
                searchEditText.clearFocus();
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

    private void LoadData() {
        if (page == 0)
            dialog.show();
        ServiceManager.getInstance().getRecipeService()
                .searchRecipe(page, Integer.parseInt(SharePref.getInstance(getApplicationContext()).getUuid()), searchEditText.getText().toString())
                .enqueue(new Callback<List<RecipeDetail>>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(Call<List<RecipeDetail>> call, Response<List<RecipeDetail>> response) {
                        if (page == 0) {
                            recipeList = response.body();
                            setRecyclerViewAdapter();
                            dialog.dismiss();
                        } else {
                            if (response.body() != null) {
                                recipeList.remove(recipeList.size() - 1);

                                int scrollPosition = recipeList.size();
                                recyclerViewAdapter.notifyItemRemoved(scrollPosition);

                                recipeList.addAll(response.body());

                                recyclerViewAdapter.notifyDataSetChanged();
                                isItemLoading = false;
                            }
                        }

                        if(response.code() == 403)
                            Toast.makeText(getApplicationContext(), "Không tìm thấy công thức trùng khớp", Toast.LENGTH_SHORT).show();
                        else if (response.body() == null) {
                            recipeList.remove(recipeList.size() - 1);
                            int scrollPosition = recipeList.size();
                            recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                            recyclerViewAdapter.notifyDataSetChanged();
                            isItemLoading = false;

                            recyclerView.clearOnScrollListeners();

                        } else {
                            page += 1;
                        }

                    }

                    @Override
                    public void onFailure(Call<List<RecipeDetail>> call, Throwable t) {
                    }
                });
    }

    private void setRecyclerViewAdapter() {
        recyclerViewAdapter = new RecipeHomeAdapter(this, recipeList);
        recyclerView.setAdapter(recyclerViewAdapter);
        initScrollListener();
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (linearLayoutManager != null) {
                    if (!isItemLoading) {
                        if (linearLayoutManager.findLastVisibleItemPosition() == recipeList.size() - 1) {
                            loadMore();
                            isItemLoading = true;
                        }
                    }
                }
            }
        });
    }

    public void loadMore() {
        recipeList.add(null);
        recyclerViewAdapter.notifyItemInserted(recipeList.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LoadData();
            }
        }, 2000);
    }

    public void hideKeyBoard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }
}
