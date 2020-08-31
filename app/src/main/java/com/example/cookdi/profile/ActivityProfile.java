package com.example.cookdi.profile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.cookdi.R;
import com.example.cookdi.Report.ReportActivity;
import com.example.cookdi.chat.common.data.model.Dialog;
import com.example.cookdi.config.Config;
import com.example.cookdi.helpers.TextHelper;
import com.example.cookdi.retrofit2.ServiceManager;
import com.example.cookdi.retrofit2.entities.RecipeDetail;
import com.example.cookdi.retrofit2.entities.User;
import com.example.cookdi.sharepref.SharePref;
import com.example.cookdi.splash.SplashActivity;
import com.example.cookdi.upload.UploadActivity;
import com.firebase.client.core.Repo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import at.favre.lib.crypto.bcrypt.BCrypt;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityProfile extends AppCompatActivity {

    private Button ChangeEmail, ChangeAvatar, ChangePass, ReportBtn;
    private Button Logout, ChangeEmailbtn, ChangePassbtn, UploadRecipeBtn;
    User user;
    private TextView userName, Email;
    private CircleImageView avatar;
    private final int PICK_IMAGE_REQUEST = 71;
    private static final String TAG = "ActivityProfile";
    UUID UUID;
    private Uri filePath;
    ImageButton backBtn;
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    String imgURL;
    Context context;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = getApplicationContext();

        fetchData();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        userName = (TextView) findViewById(R.id.username_profile);
        ChangeEmail = (Button) findViewById(R.id.Changeemailbtn);
        Logout = (Button) findViewById(R.id.logoutbtn);
        Email = (TextView) findViewById(R.id.email_profile);
        avatar = (CircleImageView) findViewById(R.id.avatar_profile);
        ChangeAvatar = (Button) findViewById(R.id.ChangeAvatarbtn);
        ChangePass = (Button) findViewById(R.id.ChangePasswordbtn);
        backBtn = findViewById(R.id.back_button);
        UploadRecipeBtn = findViewById(R.id.upload_btn);
        ReportBtn = findViewById(R.id.report_btn);
        ratingBar = findViewById(R.id.barRatingPr);

        ReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityProfile.this, ReportActivity.class));
            }
        });

        UploadRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityProfile.this, UploadActivity.class));
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityProfile.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.popupchangename, viewGroup, false);
                builder.setView(dialogView);
                final EditText changeEmail_edt = (EditText) dialogView.findViewById(R.id.Edit_email);
                changeEmail_edt.setText(user.getEmail());
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                ChangeEmailbtn = (Button) dialogView.findViewById(R.id.changebtn_dialog);
                ChangeEmailbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(TextHelper.isTextEmpty(changeEmail_edt.getText().toString())){
                            Toast.makeText(getApplicationContext(), "Email empty", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Map<String, Object> params = new HashMap<>();
                            params.put("user_id", SharePref.getInstance(getApplicationContext()).getUuid());
                            params.put("user_email", changeEmail_edt.getText().toString());

                            ServiceManager.getInstance().getUserService().editUserEmail(params).enqueue(new Callback<Map<String, String>>() {
                                @Override
                                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                                    Email.setText(changeEmail_edt.getText().toString());
                                    user.setEmail(changeEmail_edt.getText().toString());
                                    alertDialog.dismiss();
                                }

                                @Override
                                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                                    t.printStackTrace();
                                }
                            });
                        }
                    }
                });
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityProfile.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.popuplogout, viewGroup, false);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();


                Button cancelBtn = dialogView.findViewById(R.id.cancel_button);
                Button logoutBtn = dialogView.findViewById(R.id.logout_btn);

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                logoutBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        final ProgressDialog progressDialog = new ProgressDialog(context);
//                        progressDialog.setTitle("Loading...");
//                        progressDialog.show();


                        SharePref.getInstance(getApplicationContext()).setUuid(null);

//                        progressDialog.dismiss();
                        alertDialog.dismiss();

                        startActivity(new Intent(ActivityProfile.this, SplashActivity.class));


                    }
                });
            }
        });

        ChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isStoragePermissionGranted()){
                    chooseImage();
                }
            }
        });

        ChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityProfile.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.popupchangepass, viewGroup, false);
                builder.setView(dialogView);
                final EditText CurrentPass = (EditText) dialogView.findViewById(R.id.currentpass);
                final EditText NewPass = (EditText) dialogView.findViewById(R.id.newpass);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                ChangePassbtn = (Button) dialogView.findViewById(R.id.changePassbtn_dialog);
                ChangePassbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(TextHelper.isTextEmpty(CurrentPass.getText().toString()) || TextHelper.isTextEmpty(NewPass.getText().toString())){
                            Toast.makeText(getApplicationContext(), "Pass empty", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Map<String, Object> params = new HashMap<>();
                            params.put("username", userName.getText().toString());
                            ServiceManager.getInstance().getUserService().authentication(params).enqueue(new Callback<Map<String, String>>() {
                                @Override
                                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                                    BCrypt.Result result = BCrypt.verifyer().verify(CurrentPass.getText().toString().toCharArray(), response.body().get("pass"));
                                    if (result.verified) {
                                        String pass = BCrypt.withDefaults().hashToString(Config.LOG_ROUND_SALT, NewPass.getText().toString().toCharArray());
                                        Map<String, Object> params = new HashMap<>();
                                        params.put("user_id", SharePref.getInstance(getApplicationContext()).getUuid());
                                        params.put("user_password", pass);

                                        ServiceManager.getInstance().getUserService().editUserPass(params).enqueue(new Callback<Map<String, String>>() {
                                            @Override
                                            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                                                alertDialog.dismiss();
                                            }

                                            @Override
                                            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                                                t.printStackTrace();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getApplicationContext(), "current password incorrect", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                                }
                            });
                        }
                    }
                });

            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            uploadImage();

        }
    }
    private void fetchData(){
        System.out.println("fech data");
        ServiceManager.getInstance().getUserService().getUserByID(SharePref.getInstance(getApplicationContext()).getUuid()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
                setData();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    private void setData(){
        userName.setText(user.getName());
        Email.setText(user.getEmail());
        Picasso.get().load(user.getAvatar()).into(avatar);
        ratingBar.setRating(user.getRate());
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {
                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
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
                                    imgURL = String.valueOf(uri);

                                    Map<String, Object> params = new HashMap<>();
                                    params.put("user_id", SharePref.getInstance(getApplicationContext()).getUuid());
                                    params.put("user_avatar_url",imgURL);

                                    ServiceManager.getInstance().getUserService().editUserAvatar(params).enqueue(new Callback<Map<String, String>>() {
                                        @Override
                                        public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                                            Picasso.get().load(imgURL).into(avatar);
                                        }

                                        @Override
                                        public void onFailure(Call<Map<String, String>> call, Throwable t) {
                                            t.printStackTrace();
                                        }
                                    });
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
}
