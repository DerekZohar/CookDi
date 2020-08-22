package com.example.cookdi.ContactFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;


import com.example.cookdi.R;
import com.example.cookdi.chat.features.demo.styled.StyledDialogsActivity;
import com.example.cookdi.chat.features.demo.styled.StyledMessagesActivity;
import com.example.cookdi.main.MainActivity;
import com.example.cookdi.retrofit2.ServiceManager;
import com.example.cookdi.retrofit2.entities.User;
import com.example.cookdi.retrofit2.entities.UserDetail;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ContactFragment extends Fragment {

    public ContactFragment() {
        // Required empty public constructor
    }
    View view;
    Context context;
//    ArrayList<Person> persons;
    private ArrayList<User> users;
    ListPersonAdapter listPersonAdapter;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (MainActivity) getActivity();

        view = inflater.inflate(R.layout.contact_fragment, container, false);
        getContactData();




        return view;
    }
    private void getContactData() {
//        final ArrayList<User> users = new ArrayList<User>();
        ServiceManager.getInstance().getUserService().getAllUsers().enqueue(new Callback<UserDetail>() {
            @Override
            public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                users = response.body().getList();

                setAdapter();
            }
            @Override
            public void onFailure(Call<UserDetail> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    private void setAdapter(){
        listPersonAdapter = new ListPersonAdapter(context, users);
        listView = (ListView)view.findViewById(R.id.listView_contact);
        listView.setAdapter(listPersonAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                StyledMessagesActivity.open(context);
            }
        });
    }
}
