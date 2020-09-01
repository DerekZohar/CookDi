package com.example.cookdi.detail;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.example.cookdi.detail.IngredientRecyclerView.IngredientRecyclerViewAdapter;
import com.example.cookdi.detail.ReviewRecyclerView.ReviewRecyclerViewAdapter;
import com.example.cookdi.detail.StepRecyclerView.StepRecyclerViewAdapter;
import com.example.cookdi.retrofit2.ServiceManager;
import com.example.cookdi.retrofit2.entities.RecipeDetailSteps;
import com.example.cookdi.retrofit2.entities.Review;
import com.example.cookdi.sharepref.SharePref;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    LinearLayout stepLinearLayout;
    ImageButton favoriteImageButton;
    ImageButton friendImageButton;
    List<Review> reviewList;
    android.app.AlertDialog dialog;
    private RecyclerView ingredientRecyclerView;
    private RecyclerView stepRecyclerView;
    private RecyclerView reviewRecyclerView;
    private ReviewRecyclerViewAdapter reviewRecyclerViewAdapter;
    private int id;
    private boolean isFavorite;
    private boolean isFriend;
    private EditText reviewEditText;
    private RatingBar reviewRatingBar;
    private RecipeDetailSteps recipeDetailSteps;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.SpotsDialog)
                .build();
        dialog.show();

        id = getIntent().getIntExtra("recipe_id", 0);

        ingredientRecyclerView = findViewById(R.id.recyclerViewIngredientsDetailedRecipe);
        stepRecyclerView = findViewById(R.id.recyclerViewStepsDetailedRecipe);
        reviewRecyclerView = findViewById(R.id.recyclerViewReviewDetailedRecipe);

        ImageButton backImageButton = findViewById(R.id.imageButtonBackDetailedRecipe);


        final EditText reviewEditText = findViewById(R.id.editTextReviewDetailedRecipe);
        reviewEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        reviewEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LoadData();

        stepLinearLayout = findViewById(R.id.linearLayoutStartStepRecyclerview);
        stepLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recipeDetailSteps.getSteps().size() > 0) {
                    Intent intent = new Intent(v.getContext(), RecipeStepActivity.class);
                    intent.putExtra("recipe_id", id);
                    v.getContext().startActivity(intent);
                }
            }
        });


    }

    protected void LoadData() {
        ServiceManager.getInstance().getRecipeService().getRecipeSteps(id, Integer.parseInt(SharePref.getInstance(getApplicationContext()).getUuid())).enqueue(new Callback<RecipeDetailSteps>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<RecipeDetailSteps> call, final Response<RecipeDetailSteps> response) {
                if (response.body() != null) {
                    reviewEditText = findViewById(R.id.editTextReviewDetailedRecipe);
                    reviewRatingBar = findViewById(R.id.barRatingReviewDetailedRecipe);
                    ImageView recipeImageView = findViewById(R.id.imageViewRecipeDetailedRecipe);
                    ImageView userImageView = findViewById(R.id.imageViewUserAvatarDetailedRecipe);
                    TextView recipeNameTextView = findViewById(R.id.textViewNameDetailedRecipe);
                    TextView usernameTextView = findViewById(R.id.textViewUsernameDetailedRecipe);
                    TextView emailTextView = findViewById(R.id.textViewEmailDetailedRecipe);
                    RatingBar ratingBar = findViewById(R.id.barRatingFoodDetailedRecipe);
                    favoriteImageButton = findViewById(R.id.imageButtonFavoriteDetailedRecipe);
                    friendImageButton = findViewById(R.id.imageButtonAddFriendDetailedRecipe);
                    Button sendButton = findViewById(R.id.buttonSendDetailedRecipe);
                    Button cancelButton = findViewById(R.id.buttonCancelDetailedRecipe);

                    recipeDetailSteps = response.body();

                    if (SharePref.getInstance(getApplicationContext()).getUuid().equals(response.body().getChef().getId().toString())) {
                        friendImageButton.setBackgroundResource(R.drawable.ic_friend_add_disabled);
                    } else {
                        CheckFriend(response.body().getChef().getId());

                        friendImageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UpdateFriend(response.body().getChef().getId());
                            }
                        });
                    }


                    isFavorite = response.body().getFavorite();
                    if (isFavorite)
                        favoriteImageButton.setBackgroundResource(R.drawable.ic_favorited);
                    else
                        favoriteImageButton.setBackgroundResource(R.drawable.ic_favorite);

                    favoriteImageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UpdateFavorite();
                        }
                    });

                    sendButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SendReview();
                        }
                    });

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CancelReview();
                        }
                    });


                    Picasso.get().load(response.body().getRecipe().getImageUrl()).error(R.drawable.ic_error).placeholder(R.drawable.ic_placeholder_background).fit().into(recipeImageView);
                    Picasso.get().load(response.body().getChef().getAvatar()).error(R.drawable.ic_error).placeholder(R.drawable.ic_placeholder_background).into(userImageView);
                    recipeNameTextView.setText(response.body().getRecipe().getRecipeName());
                    usernameTextView.setText(response.body().getChef().getName());
                    emailTextView.setText(response.body().getChef().getEmail());
                    ratingBar.setRating(response.body().getRecipe().getRating());


                    ingredientRecyclerView.setAdapter(new IngredientRecyclerViewAdapter(getApplicationContext(), response.body().getIngredients()));
                    ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    stepRecyclerView.setAdapter(new StepRecyclerViewAdapter(getApplicationContext(), response.body().getSteps()));
                    stepRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Không nhận được phản hồi từ máy chủ!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecipeDetailSteps> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Không nhận được phản hồi từ máy chủ!", Toast.LENGTH_SHORT).show();
            }
        });

        ServiceManager.getInstance().getReviewService().getAllReview(id).enqueue(new Callback<List<Review>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (response.body() != null) {
                    reviewList = response.body();
                    Collections.reverse(reviewList);
                    reviewRecyclerViewAdapter = new ReviewRecyclerViewAdapter(getApplicationContext(), reviewList);
                    reviewRecyclerView.setAdapter(reviewRecyclerViewAdapter);
                    reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                } else {
                    Toast.makeText(getApplicationContext(), "Không nhận được phản hồi từ máy chủ!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Không nhận được phản hồi từ máy chủ!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void CheckFriend(int id) {
        ServiceManager.getInstance().getFriendService().isFriend(SharePref.getInstance(getApplicationContext()).getUuid(), id).enqueue(new Callback<Boolean>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body() != null)
                    if (!response.body()) {
                        isFriend = false;
                        friendImageButton.setBackgroundResource(R.drawable.ic_friend_add);
                    } else {
                        isFriend = true;
                        friendImageButton.setBackgroundResource(R.drawable.ic_friend_added);
                    }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Không nhận được phản hồi từ máy chủ!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SendReview() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", SharePref.getInstance(getApplicationContext()).getUuid());
        params.put("recipe_id", Integer.toString(id));
        params.put("comment_content", reviewEditText.getText().toString());
        params.put("rate", Float.toString(reviewRatingBar.getRating()));

        if (reviewRatingBar.getRating() == 0)
            Toast.makeText(getApplicationContext(), "Vui lòng đánh giá số sao!", Toast.LENGTH_SHORT).show();
        else if (reviewEditText.getText().toString().isEmpty())
            Toast.makeText(getApplicationContext(), "Vui lòng nhập đánh giá!", Toast.LENGTH_SHORT).show();
        else
            try {
                ServiceManager.getInstance().getReviewService().addReview(params).enqueue(new Callback<Map<String, String>>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                        if (response.body() == null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                            ViewGroup viewGroup = findViewById(android.R.id.content);
                            View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popup_review, viewGroup, false);
                            builder.setView(dialogView);
                            final AlertDialog alertDialog = builder.create();
                            alertDialog.show();


                            Button cancelButton = dialogView.findViewById(R.id.buttonCancelReviewPopup);
                            Button agreeButton = dialogView.findViewById(R.id.buttonAgreeReviewPopup);

                            cancelButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alertDialog.dismiss();
                                }
                            });
                            agreeButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ResendReview();
                                    alertDialog.dismiss();
                                }
                            });
                        } else if (Objects.requireNonNull(response.body().get("message")).equals("Success")) {
                            UpdateReview();
                            Toast.makeText(getApplicationContext(), "Gửi đánh giá thành công!", Toast.LENGTH_SHORT).show();
                            reviewEditText.setText("");
                            reviewRatingBar.setRating(0);
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, String>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Không nhận được phản hồi từ máy chủ!", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Đã xảy ra sự cố!", Toast.LENGTH_SHORT).show();
            }
    }

    private void CancelReview() {
        reviewEditText.setText("");
        reviewRatingBar.setRating(0);
    }

    private void ResendReview() {
        final Map<String, Object> params = new HashMap<>();
        params.put("user_id", SharePref.getInstance(getApplicationContext()).getUuid());
        params.put("recipe_id", Integer.toString(id));
        params.put("comment_content", reviewEditText.getText().toString());
        params.put("rate", Float.toString(reviewRatingBar.getRating()));

        try {
            ServiceManager.getInstance().getReviewService().removeReview(SharePref.getInstance(getApplicationContext()).getUuid(), id).enqueue(new Callback<Map<String, String>>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if (response.body() == null) {
                        Toast.makeText(getApplicationContext(), "Không nhận được phản hồi từ máy chủ!", Toast.LENGTH_SHORT).show();
                    } else if (Objects.requireNonNull(response.body().get("message")).equals("Success")) {
                        ServiceManager.getInstance().getReviewService().addReview(params).enqueue(new Callback<Map<String, String>>() {
                            @Override
                            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                                UpdateReview();
                                Toast.makeText(getApplicationContext(), "Gửi đánh giá thành công!", Toast.LENGTH_SHORT).show();
                                reviewEditText.setText("");
                                reviewRatingBar.setRating(0);
                            }

                            @Override
                            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "Không nhận được phản hồi từ máy chủ!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Không nhận được phản hồi từ máy chủ!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Đã xảy ra sự cố!", Toast.LENGTH_SHORT).show();
        }
    }

    private void UpdateFavorite() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", SharePref.getInstance(getApplicationContext()).getUuid());
        params.put("recipe_id", id);

        if (!isFavorite)
            ServiceManager.getInstance().getFavoriteService().addToFavorite(params).enqueue(new Callback<Map<String, String>>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if (response.body() == null)
                        Toast.makeText(getApplicationContext(), "Không nhận được phản hồi từ máy chủ!", Toast.LENGTH_SHORT).show();
                    else {
                        isFavorite = !isFavorite;
                        favoriteImageButton.setBackgroundResource(R.drawable.ic_favorited);
                    }
                }

                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Không nhận được phản hồi từ máy chủ!", Toast.LENGTH_SHORT).show();
                }
            });
        else
            ServiceManager.getInstance().getFavoriteService().delete(params).enqueue(new Callback<Map<String, String>>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if (response.body() == null)
                        Toast.makeText(getApplicationContext(), "Không nhận được phản hồi từ máy chủ!", Toast.LENGTH_SHORT).show();
                    else {
                        isFavorite = !isFavorite;
                        favoriteImageButton.setBackgroundResource(R.drawable.ic_favorite);
                    }
                }

                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Không nhận được phản hồi từ máy chủ!", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void UpdateFriend(int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id_1", SharePref.getInstance(getApplicationContext()).getUuid());
        params.put("user_id_2", id);

        if (!isFriend)
            ServiceManager.getInstance().getFriendService().addFriend(params).enqueue(new Callback<Map<String, String>>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if (response.body() == null)
                        Toast.makeText(getApplicationContext(), "Không nhận được phản hồi từ máy chủ!", Toast.LENGTH_SHORT).show();
                    else {
                        isFriend = !isFriend;
                        friendImageButton.setBackgroundResource(R.drawable.ic_friend_added);
                    }
                }

                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Không nhận được phản hồi từ máy chủ!", Toast.LENGTH_SHORT).show();
                }
            });
        else
            ServiceManager.getInstance().getFriendService().deleteFriend(params).enqueue(new Callback<Map<String, String>>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if (response.body() == null)
                        Toast.makeText(getApplicationContext(), "Không nhận được phản hồi từ máy chủ!", Toast.LENGTH_SHORT).show();
                    else {
                        isFriend = !isFriend;
                        friendImageButton.setBackgroundResource(R.drawable.ic_friend_add);
                    }
                }

                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Không nhận được phản hồi từ máy chủ!", Toast.LENGTH_SHORT).show();
                }
            });

    }

    private void UpdateReview() {
        ServiceManager.getInstance().getReviewService().getAllReview(id).enqueue(new Callback<List<Review>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (response.body() == null)
                    Toast.makeText(getApplicationContext(), "Không nhận được phản hồi từ máy chủ!", Toast.LENGTH_SHORT).show();
                else {
                    reviewList.clear();
                    reviewList.addAll(response.body());
                    Collections.reverse(reviewList);
                    reviewRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Không nhận được phản hồi từ máy chủ!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}