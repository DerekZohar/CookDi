package com.example.cookdi.upload;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookdi.R;
import com.example.cookdi.helpers.TextHelper;
import com.example.cookdi.retrofit2.ServiceManager;
import com.example.cookdi.retrofit2.entities.Ingredient;
import com.example.cookdi.retrofit2.entities.Recipe;
import com.example.cookdi.retrofit2.entities.RecipeDetailSteps;
import com.example.cookdi.retrofit2.entities.RecipeStep;
import com.example.cookdi.sharepref.SharePref;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadActivity extends AppCompatActivity {

    static class UpImageType {
        static int STEP = 0;
        static int RECIPE = 1;
    }

    Button publishButton;
    ImageButton stepAttach;
    EditText textIn,textInStep,stepTime, foodName, description, tag;
    FloatingActionButton buttonAdd, addstepbutton, addTag;
    LinearLayout container, containerstep, tagContainer;
    TextView DisplayTime;
    ImageView recipeImage;

    int stephours, stepminutes;
    String stepImageUrl = "";
    String recipeImageUrl ="";
    RecipeDetailSteps detailSteps = new RecipeDetailSteps();
    ArrayList<String> tags = new ArrayList<>();

    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    private int upImageType = -1;

    //firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    UUID UUID;
    String imgURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        textIn = (EditText)findViewById(R.id.textin);
        stepTime = (EditText)findViewById(R.id.steptime);
        foodName = (EditText)findViewById(R.id.foodname);
        description = (EditText)findViewById(R.id.description);
        tag = (EditText)findViewById(R.id.tag);
        buttonAdd = (FloatingActionButton) findViewById(R.id.add);
        container = (LinearLayout)findViewById(R.id.container);
        tagContainer = (LinearLayout)findViewById(R.id.tagContainer);
        containerstep = (LinearLayout)findViewById(R.id.containerstep);
        addstepbutton = (FloatingActionButton) findViewById(R.id.addstep);
        addTag = (FloatingActionButton) findViewById(R.id.addTag);
        textInStep = (EditText)findViewById(R.id.textinstep);
        stepAttach = findViewById(R.id.stepAttach);
        recipeImage = findViewById(R.id.recipeImage);
        publishButton = findViewById(R.id.publishButton);


        stepAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upImageType = UpImageType.STEP;
                chooseImage();
            }
        });

        recipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upImageType = UpImageType.RECIPE;
                chooseImage();
            }
        });

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPublishRecipeButtonClicked();
            }
        });

        addTag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.ingredient, null);
                final TextView textOut = (TextView)addView.findViewById(R.id.textout);
                textOut.setText(tag.getText().toString());
                ImageButton buttonRemove = (ImageButton) addView.findViewById(R.id.remove);

                final View.OnClickListener thisListener = new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        ((LinearLayout)addView.getParent()).removeView(addView);
                        tags.remove(tag.getText().toString());
                    }
                };

                buttonRemove.setOnClickListener(thisListener);
                tagContainer.addView(addView);
                tags.add(tag.getText().toString());
                tag.setText("");
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.ingredient, null);
                final TextView textOut = (TextView)addView.findViewById(R.id.textout);
                textOut.setText(textIn.getText().toString());
                ImageButton buttonRemove = (ImageButton) addView.findViewById(R.id.remove);

                final View.OnClickListener thisListener = new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        ((LinearLayout)addView.getParent()).removeView(addView);
                        ArrayList<Ingredient> ingredients = detailSteps.getIngredients();
                        for (int i =0;i<ingredients.size() ; ++i) {
                            if (ingredients.get(i).getIngredient().compareTo(textOut.getText().toString()) == 0) {
                                ingredients.remove(ingredients.get(i));
                                break;
                            }
                         }
                        detailSteps.setIngredients(ingredients);
                    }
                };

                buttonRemove.setOnClickListener(thisListener);
                container.addView(addView);


                ArrayList<Ingredient> ingredients = detailSteps.getIngredients();
                if (ingredients == null) {
                    ingredients = new ArrayList<Ingredient>();
                }
                Ingredient ingredient = new Ingredient();
                ingredient.setIngredient(textIn.getText().toString());
                ingredients.add(ingredient);
                detailSteps.setIngredients(ingredients);

                textIn.setText("");
            }
        });
        DisplayTime = (EditText) findViewById(R.id.steptime);
        DisplayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog pickerDialog = new TimePickerDialog(UploadActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        stephours = hourOfDay;
                        stepminutes = minute;
                        DisplayTime.setText(stephours+":"+stepminutes);
                    }
                },24,0,true);
                pickerDialog.updateTime(stephours,stepminutes);
                pickerDialog.show();
            }
        });

        addstepbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.steps, null);
                final TextView textOutStep = (TextView)addView.findViewById(R.id.textoutstep);
                textOutStep.setText(textInStep.getText().toString());
                TextView steptime = (TextView)addView.findViewById(R.id.texttime);
                steptime.setText(stepTime.getText().toString());
                ImageButton buttonRemove = (ImageButton) addView.findViewById(R.id.removestep);

                final View.OnClickListener thisListener = new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        ((LinearLayout)addView.getParent()).removeView(addView);
                        ArrayList<RecipeStep> recipeSteps = detailSteps.getSteps();

                        for (int i =0;i<recipeSteps.size() ; ++i) {
                            if (recipeSteps.get(i).getStep_description().compareTo(textOutStep.getText().toString()) == 0) {
                                recipeSteps.remove(recipeSteps.get(i));
                                break;
                            }
                        }
                        detailSteps.setSteps(recipeSteps);
                    }
                };

                buttonRemove.setOnClickListener(thisListener);
                containerstep.addView(addView);


                ArrayList<RecipeStep> recipeSteps = detailSteps.getSteps();
                if (recipeSteps == null) {
                    recipeSteps = new ArrayList<RecipeStep>();
                }

                RecipeStep step = new RecipeStep();
                step.setStep_description(textInStep.getText().toString());
                step.setDuration_minute(stephours*60 + stepminutes);
                step.setStep_image_url(stepImageUrl);
                stepImageUrl = "";
                recipeSteps.add(step);
                detailSteps.setSteps(recipeSteps);
                textInStep.setText("");
                stepTime.setText("");
                stephours = 0;
                stepminutes = 0;
            }
        });
    }
    private void uploadImage() {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            UUID = UUID.randomUUID();

            final StorageReference ref = storageReference.child("images/"+ UUID.toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //url of img on firebase
                                    imgURL = String.valueOf(uri);
                                    if (upImageType == UpImageType.STEP) {
                                        stepImageUrl = imgURL;
                                    } else {
                                        recipeImageUrl = imgURL;
                                        Picasso.get().load(imgURL).into(recipeImage);
                                    }
                                    upImageType = -1;
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });

        }

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            //here
            filePath = data.getData();
            uploadImage();
        }
    }

    private void onPublishRecipeButtonClicked() {
        if (TextHelper.isTextEmpty(foodName.getText().toString())) {
            Toast.makeText(getApplicationContext(), "You must enter recipe name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextHelper.isTextEmpty(description.getText().toString())) {
            Toast.makeText(getApplicationContext(), "You must enter recipe description", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tags.size() == 0) {
            Toast.makeText(getApplicationContext(), "You must enter recipe tag", Toast.LENGTH_SHORT).show();
            return;
        }
        if (detailSteps.getIngredients().size() == 0) {
            Toast.makeText(getApplicationContext(), "You must enter recipe ingredient", Toast.LENGTH_SHORT).show();
            return;
        }
        if (detailSteps.getSteps().size() == 0) {
            Toast.makeText(getApplicationContext(), "You must enter recipe step", Toast.LENGTH_SHORT).show();
            return;
        }



        HashMap<String, Object> params = new HashMap<>();
        params.put("recipe_name", foodName.getText().toString());
        params.put("user_id", SharePref.getInstance(getApplicationContext()).getUuid());
        params.put("recipe_description", description.getText().toString());
        params.put("image_url", recipeImageUrl);

        ArrayList<String> ingredients = new ArrayList<String>();
        for (int i = 0; i < detailSteps.getIngredients().size(); i++) {
            ingredients.add(detailSteps.getIngredients().get(i).getIngredient());
        }

        int totalTime = 0;

        ArrayList<Map<String, Object>> steps = new ArrayList<Map<String, Object>>();
        Map<String, Object> step = new HashMap<>();
        for (int i = 0; i < detailSteps.getSteps().size(); i++) {
            step.put("stepDescription", detailSteps.getSteps().get(i).getStep_description());
            step.put("stepDuration", detailSteps.getSteps().get(i).getDuration_minute());
            step.put("stepImageUrl", detailSteps.getSteps().get(i).getStep_image_url());
            totalTime += detailSteps.getSteps().get(i).getDuration_minute();
            steps.add(step);
        }

        params.put("recipe_time", totalTime);
        params.put("recipeSteps", steps);
        params.put("recipeIngredients", ingredients);
        params.put("tags", tags);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        ServiceManager.getInstance().getRecipeService().addRecipe(params).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
