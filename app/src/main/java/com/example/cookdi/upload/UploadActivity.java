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
import com.example.cookdi.retrofit2.entities.Ingredient;
import com.example.cookdi.retrofit2.entities.Recipe;
import com.example.cookdi.retrofit2.entities.RecipeDetailSteps;
import com.example.cookdi.retrofit2.entities.RecipeStep;
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
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {

    static class UpImageType {
        static int STEP = 0;
        static int RECIPE = 1;
    }

    ImageButton stepAttach, recipeImageAttach;
    EditText textIn,textInStep,stepTime;
    FloatingActionButton buttonAdd, addstepbutton;
    LinearLayout container, containerstep;
    TextView DisplayTime;
    ImageView recipeImage;

    int stephours, stepminutes;
    String stepImageUrl = "";
    String recipeImageUrl ="";
    RecipeDetailSteps detailSteps = new RecipeDetailSteps();
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
        buttonAdd = (FloatingActionButton) findViewById(R.id.add);
        container = (LinearLayout)findViewById(R.id.container);
        containerstep = (LinearLayout)findViewById(R.id.containerstep);
        addstepbutton = (FloatingActionButton) findViewById(R.id.addstep);
        textInStep = (EditText)findViewById(R.id.textinstep);
        stepAttach = findViewById(R.id.stepAttach);
        recipeImageAttach = findViewById(R.id.recipeImageAttach);
        recipeImage = findViewById(R.id.recipeImage);


        stepAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upImageType = UpImageType.STEP;
                chooseImage();
            }
        });
        recipeImageAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upImageType = UpImageType.RECIPE;
                chooseImage();
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
                FloatingActionButton buttonRemove = (FloatingActionButton) addView.findViewById(R.id.remove);

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
                FloatingActionButton buttonRemove = (FloatingActionButton) addView.findViewById(R.id.removestep);

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
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();

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
    public void hideKeyBoard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

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
}
