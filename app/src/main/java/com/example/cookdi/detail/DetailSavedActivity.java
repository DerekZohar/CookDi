package com.example.cookdi.detail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookdi.R;
import com.example.cookdi.RecipeStep.RecipeStepActivity;
import com.example.cookdi.Report.ReportActivity;
import com.example.cookdi.db.IngredientDBAdapter;
import com.example.cookdi.db.RecipeListDBAdapter;
import com.example.cookdi.db.RecipeStepDBAdapter;
import com.example.cookdi.db.UserListDBAdapter;
import com.example.cookdi.detail.IngredientRecyclerView.IngredientRecyclerViewAdapter;
import com.example.cookdi.detail.ReviewRecyclerView.ReviewRecyclerViewAdapter;
import com.example.cookdi.detail.StepRecyclerView.StepRecyclerViewAdapter;
import com.example.cookdi.retrofit2.ServiceManager;
import com.example.cookdi.retrofit2.entities.Ingredient;
import com.example.cookdi.retrofit2.entities.RecipeDetailSteps;
import com.example.cookdi.retrofit2.entities.RecipeStep;
import com.example.cookdi.retrofit2.entities.Review;
import com.example.cookdi.retrofit2.entities.SavedRecipe;
import com.example.cookdi.retrofit2.entities.SavedRecipeDetailSteps;
import com.example.cookdi.retrofit2.entities.SavedRecipeStep;
import com.example.cookdi.retrofit2.entities.SavedUser;
import com.example.cookdi.sharepref.SharePref;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailSavedActivity extends AppCompatActivity {

    LinearLayout stepLinearLayout;
    private RecyclerView ingredientRecyclerView;
    private RecyclerView stepRecyclerView;
    private int recipe_id;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_detail);

        recipe_id = getIntent().getIntExtra("recipe_id", 0);

        ingredientRecyclerView = findViewById(R.id.recyclerViewIngredientsDetailedSavedRecipe);
        stepRecyclerView = findViewById(R.id.recyclerViewStepsDetailedSavedRecipe);
        stepLinearLayout = findViewById(R.id.linearLayoutStartStepRecyclerview);
        ImageButton backImageButton = findViewById(R.id.imageButtonBackDetailedSavedRecipe);

        stepLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RecipeStepActivity.class);
                intent.putExtra("recipe_id", recipe_id);
                v.getContext().startActivity(intent);
            }
        });

        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LoadData();
    }

    protected void LoadData() {
        SavedRecipeDetailSteps recipeDetailSteps = new SavedRecipeDetailSteps();

        SavedRecipe savedRecipe = RecipeListDBAdapter.getRecipeById(recipe_id);
        SavedUser savedUser = UserListDBAdapter.getUserById(savedRecipe.getUserId());
        ArrayList<SavedRecipeStep> savedRecipeSteps = RecipeStepDBAdapter.getAllRecipeStepById(recipe_id);
        ArrayList<Ingredient> ingredients = IngredientDBAdapter.getAllIngredientById(recipe_id);
        recipeDetailSteps.setRecipe(savedRecipe);
        recipeDetailSteps.setChef(savedUser);
        recipeDetailSteps.setSavedRecipeSteps(savedRecipeSteps);
        recipeDetailSteps.setIngredients(ingredients);

        ImageView recipeImageView = findViewById(R.id.imageViewRecipeDetailedSavedRecipe);
        ImageView userImageView = findViewById(R.id.imageViewUserAvatarDetailedSavedRecipe);
        TextView recipeNameTextView = findViewById(R.id.textViewNameDetailedSavedRecipe);
        TextView usernameTextView = findViewById(R.id.textViewUsernameDetailedSavedRecipe);
        TextView emailTextView = findViewById(R.id.textViewEmailDetailedSavedRecipe);
        RatingBar ratingBar = findViewById(R.id.barRatingFoodDetailedSavedRecipe);

        InputStream inputStream  = new ByteArrayInputStream(savedRecipe.getImage());
        Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
        recipeImageView.setImageBitmap(bitmap);

        inputStream = new ByteArrayInputStream(savedUser.getImage());
        bitmap = BitmapFactory.decodeStream(inputStream);
        userImageView.setImageBitmap(bitmap);

        recipeNameTextView.setText(savedRecipe.getRecipeName());
        usernameTextView.setText(savedUser.getName());
        emailTextView.setText(savedUser.getEmail());
        ratingBar.setRating(savedRecipe.getRating());


        ingredientRecyclerView.setAdapter(new IngredientRecyclerViewAdapter(getApplicationContext(), recipeDetailSteps.getIngredients()));
        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        stepRecyclerView.setAdapter(new StepRecyclerViewAdapter(getApplicationContext(), recipeDetailSteps.getSavedRecipeSteps()));
        stepRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }
}