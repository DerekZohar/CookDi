package com.example.cookdi.edit_recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.cookdi.R;
import com.example.cookdi.helpers.TextHelper;
import com.example.cookdi.retrofit2.ServiceManager;
import com.example.cookdi.retrofit2.entities.Ingredient;
import com.example.cookdi.retrofit2.entities.RecipeDetailSteps;
import com.example.cookdi.retrofit2.entities.RecipeStep;
import com.example.cookdi.sharepref.SharePref;
import com.example.cookdi.upload.UploadActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HTTP;

public class EditRecipe extends AppCompatActivity {

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
    ProgressDialog progressDialog;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    private int upImageType = -1;

    //firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    java.util.UUID UUID;
    String imgURL;

    private int recipeId;

    public static void open(Context context, int recipeId) {
        Intent intent = new Intent(context, EditRecipe.class);
        intent.putExtra("recipe_id", recipeId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        progressDialog = new ProgressDialog(this);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        textIn = (EditText)this.findViewById(R.id.textin);
        stepTime = (EditText)this.findViewById(R.id.steptime);
        foodName = (EditText)this.findViewById(R.id.foodname);
        description = (EditText)this.findViewById(R.id.description);
        tag = (EditText)this.findViewById(R.id.tag);
        buttonAdd = (FloatingActionButton) this.findViewById(R.id.add);
        container = (LinearLayout)this.findViewById(R.id.container);
        tagContainer = (LinearLayout)this.findViewById(R.id.tagContainer);
        containerstep = (LinearLayout)this.findViewById(R.id.containerstep);
        addstepbutton = (FloatingActionButton) this.findViewById(R.id.addstep);
        addTag = (FloatingActionButton) this.findViewById(R.id.addTag);
        textInStep = (EditText)this.findViewById(R.id.textinstep);
        stepAttach = this.findViewById(R.id.stepAttach);
        recipeImage = this.findViewById(R.id.recipeImage);
        publishButton = this.findViewById(R.id.publishButton);

        Bundle b = getIntent().getExtras();
        recipeId = b.getInt("recipe_id");

        setButtonClicked();
        getData();
    }

    private void getData() {
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        ServiceManager.getInstance().getRecipeService().getRecipeSteps(recipeId).enqueue(new Callback<RecipeDetailSteps>() {
            @Override
            public void onResponse(Call<RecipeDetailSteps> call, Response<RecipeDetailSteps> response) {
                if (response.code() == 200) {
                    setupData(response.body());
                } else {
                    Toast.makeText(getApplicationContext(), "Have error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecipeDetailSteps> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Have error", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        });
    }

    private void setupData(RecipeDetailSteps recipeDetailSteps) {
        foodName.setText(recipeDetailSteps.getRecipe().getRecipeName());
        description.setText(recipeDetailSteps.getRecipe().getDescription());
        for (int i =0;i<recipeDetailSteps.getTags().size(); i++) {
            addTagView(recipeDetailSteps.getTags().get(i).getIngredient());
        }
        for (int i =0;i<recipeDetailSteps.getIngredients().size(); i++) {
            addIngredient(recipeDetailSteps.getIngredients().get(i).getIngredient());
        }
        for (int i =0;i<recipeDetailSteps.getSteps().size(); i++) {
            addStep(recipeDetailSteps.getSteps().get(i).getStep_description(), recipeDetailSteps.getSteps().get(i).getDuration_minute(), recipeDetailSteps.getSteps().get(i).getStep_image_url());
        }
        if (!TextHelper.isTextEmpty(recipeDetailSteps.getRecipe().getImageUrl())) {
            Picasso.get().load(recipeDetailSteps.getRecipe().getImageUrl()).into(recipeImage);
        }

        progressDialog.hide();
    }

    private void addTagView(final String text) {
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.ingredient, null);
        final TextView textOut = (TextView)addView.findViewById(R.id.textout);
        textOut.setText(text);
        ImageButton buttonRemove = (ImageButton) addView.findViewById(R.id.remove);

        final View.OnClickListener thisListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((LinearLayout)addView.getParent()).removeView(addView);
                tags.remove(text);
            }
        };

        buttonRemove.setOnClickListener(thisListener);
        tagContainer.addView(addView);
        tags.add(text);
    }

    private void addIngredient(final String text) {
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.ingredient, null);
        final TextView textOut = (TextView)addView.findViewById(R.id.textout);
        textOut.setText(text);
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
        ingredient.setIngredient(text);
        ingredients.add(ingredient);
        detailSteps.setIngredients(ingredients);

    }

    private void addStep(final String description, final int duration, final String imageUrl) {
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.steps, null);
        final TextView textOutStep = (TextView)addView.findViewById(R.id.textoutstep);
        final TextView textTime = (TextView)addView.findViewById(R.id.texttime);
        textOutStep.setText(description);
        textTime.setText(String.valueOf(duration));
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
        step.setStep_description(description);
        step.setDuration_minute(duration);
        step.setStep_image_url(imageUrl);
        recipeSteps.add(step);
        detailSteps.setSteps(recipeSteps);
    }


    private void setButtonClicked() {
        stepAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upImageType = EditRecipe.UpImageType.STEP;
                chooseImage();
            }
        });

        recipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upImageType = EditRecipe.UpImageType.RECIPE;
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
                addTagView(tag.getText().toString());
                tag.setText("");
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addIngredient(textIn.getText().toString());
                textIn.setText("");
            }
        });
        DisplayTime = (EditText) findViewById(R.id.steptime);
        DisplayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog pickerDialog = new TimePickerDialog(EditRecipe.this, new TimePickerDialog.OnTimeSetListener() {
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
                addStep(textInStep.getText().toString(), stephours*60 + stepminutes, stepImageUrl);
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
                                    if (upImageType == EditRecipe.UpImageType.STEP) {
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
        params.put("recipe_id", recipeId);
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
        ServiceManager.getInstance().getRecipeService().updateRecipe(params).enqueue(new Callback<Map<String, String>>() {
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