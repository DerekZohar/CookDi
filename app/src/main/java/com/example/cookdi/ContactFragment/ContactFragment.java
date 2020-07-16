package com.example.cookdi.ContactFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

import com.example.cookdi.MainActivity;
import com.example.cookdi.R;


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
        Person cao_trong_tin = new Person("Tran Thuan Thanh", "13123123123123131231231231231312313", "user");
        Person  nguyen_huu_tuan= new Person("Nguyen Huu Tuan", "13123123123123131231231231231312313", "user1");
        Person le_tan_thinh = new Person("Nguyen Thanh Truong", "13123123123123131231231231231312313", "user2");
        Person ngo_viet_thang = new Person("Ngo Viet Thang", "13123123123123131231231231231312313", "user3");
        Person ha_lu = new Person("Dang Duc Trung", "13123123123123131231231231231312313", "user3");


        list.add(cao_trong_tin);
        list.add(nguyen_huu_tuan);
        list.add(le_tan_thinh);
        list.add(ngo_viet_thang);
        list.add(ha_lu);
        list.add(cao_trong_tin);
        list.add(nguyen_huu_tuan);
        list.add(le_tan_thinh);
        list.add(ngo_viet_thang);
        list.add(ha_lu);
        list.add(cao_trong_tin);
        list.add(nguyen_huu_tuan);
        list.add(le_tan_thinh);
        list.add(ngo_viet_thang);
        list.add(ha_lu);

        return list;
    }
}
