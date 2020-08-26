package com.example.cookdi.profile;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookdi.R;
import com.example.cookdi.retrofit2.ServiceManager;
import com.example.cookdi.retrofit2.entities.RecipeDetail;
import com.example.cookdi.retrofit2.entities.User;
import com.example.cookdi.sharepref.SharePref;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityProfile extends AppCompatActivity {

    private Button Changename;
    private Button Logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fetchData();

        Changename = (Button) findViewById(R.id.Changenamebtn);
        Logout = (Button) findViewById(R.id.logoutbtn);

        Changename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityProfile.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.popupchangename, viewGroup, false);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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
//        ServiceManager.getInstance().getUserService().getUserById(SharePref.getInstance(getApplicationContext()).getUuid()).enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                System.out.println("_________________");
//                System.out.println(response.body().getName());
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                System.out.println("_________________");
//                t.printStackTrace();
//            }
//        });
        ServiceManager.getInstance().getRecipeService().getAllRecipe(0).enqueue(new Callback<List<RecipeDetail>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<RecipeDetail>> call, Response<List<RecipeDetail>> response) {
                System.out.println("_________________1");
            }

            @Override
            public void onFailure(Call<List<RecipeDetail>> call, Throwable t) {
                System.out.println("_________________2");
            }
        });
    }
}
