package com.example.cookdi.ContactFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;


import com.example.cookdi.R;
import com.example.cookdi.main.MainActivity;

import java.util.ArrayList;


public class ContactFragment extends Fragment {

    public ContactFragment() {
        // Required empty public constructor
    }
    View view;
    Context context;
    ArrayList<Person> persons;
    ListPersonAdapter listPersonAdapter;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (MainActivity) getActivity();
        persons = getListData();
        view = inflater.inflate(R.layout.contact_fragment, container, false);

        listPersonAdapter = new ListPersonAdapter(context, persons);
        listView = (ListView)view.findViewById(R.id.listView_contact);
        listView.setAdapter(listPersonAdapter);

        return view;
    }
    private ArrayList<Person> getListData() {
        ArrayList<Person> list = new ArrayList<Person>();
        Person tran_thuan_thanh = new Person("Tran Thuan Thanh", "13123123123123131231231231231312313", "user");
        Person nguyen_huu_tuan= new Person("Nguyen Huu Tuan", "13123123123123131231231231231312313", "user1");
        Person nguyen_thanh_truong = new Person("Nguyen Thanh Truong", "13123123123123131231231231231312313", "user2");
        Person ngo_viet_thang = new Person("Ngo Viet Thang", "13123123123123131231231231231312313", "user3");
        Person dang_duc_trung = new Person("Dang Duc Trung", "13123123123123131231231231231312313", "user3");


        list.add(tran_thuan_thanh);
        list.add(nguyen_huu_tuan);
        list.add(nguyen_thanh_truong);
        list.add(ngo_viet_thang);
        list.add(dang_duc_trung);
        list.add(tran_thuan_thanh);
        list.add(nguyen_huu_tuan);
        list.add(nguyen_thanh_truong);
        list.add(ngo_viet_thang);
        list.add(dang_duc_trung);
        list.add(tran_thuan_thanh);
        list.add(nguyen_huu_tuan);
        list.add(nguyen_thanh_truong);
        list.add(ngo_viet_thang);
        list.add(dang_duc_trung);

        return list;
    }
}
