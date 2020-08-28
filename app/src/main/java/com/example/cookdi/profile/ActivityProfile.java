package com.example.cookdi.profile;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookdi.R;
import com.example.cookdi.retrofit2.ServiceManager;
import com.example.cookdi.retrofit2.entities.RecipeDetail;
import com.example.cookdi.retrofit2.entities.User;
import com.example.cookdi.sharepref.SharePref;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityProfile extends AppCompatActivity {

    private Button Changename;
    private Button Logout, ChangeEmailbtn;
    User user;
    private TextView userName, Email;
    private CircleImageView avatar;
    private EditText EditName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fetchData();

        userName = (TextView) findViewById(R.id.username_profile);
        Changename = (Button) findViewById(R.id.Changenamebtn);
        Logout = (Button) findViewById(R.id.logoutbtn);
        Email = (TextView) findViewById(R.id.email_profile);
        avatar = (CircleImageView) findViewById(R.id.avatar_profile);




        Changename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityProfile.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.popupchangename, viewGroup, false);
                builder.setView(dialogView);
                final EditText changeEmail_edt = (EditText) dialogView.findViewById(R.id.Edit_email);
                changeEmail_edt.setText(user.getEmail());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                ChangeEmailbtn = (Button) dialogView.findViewById(R.id.changebtn_dialog);
                ChangeEmailbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> params = new HashMap<>();
                        params.put("user_id", SharePref.getInstance(getApplicationContext()).getUuid());
                        params.put("user_email",changeEmail_edt.getText().toString());

                        ServiceManager.getInstance().getUserService().editUserEmail(params).enqueue(new Callback<Map<String, String>>() {
                            @Override
                            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                                System.out.println("____________1");
                                Email.setText(changeEmail_edt.getText().toString());
                            }

                            @Override
                            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                                System.out.println("____________2");
                                t.printStackTrace();
                            }
                        });
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
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


    }
    private void fetchData(){
        System.out.println("____________");
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
    }
}
