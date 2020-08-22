package com.example.cookdi.ContactFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cookdi.R;
import com.example.cookdi.retrofit2.entities.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListPersonAdapter extends BaseAdapter {
    private List<User> persons;
    private LayoutInflater layoutInflater;
    private Context context;

    public ListPersonAdapter(Context aContext, List<User> personList){
        this.context = aContext;
        this.persons = personList;
        layoutInflater = LayoutInflater.from(aContext);

    }
    @Override
    public int getCount() {
        return persons.size();
    }

    @Override
    public Object getItem(int position) {
        return persons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.fragment_contact_person_item, null);

            holder = new ViewHolder();

            holder.avatar = (ImageView) convertView.findViewById(R.id.imageView_user);
            holder.nameView = (TextView) convertView.findViewById(R.id.textView_name);
            holder.stateView = (TextView) convertView.findViewById(R.id.textView_phone);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        User person = this.persons.get(position);
        holder.nameView.setText(person.getName());

        holder.stateView.setText("messages");

        Picasso.get().load(person.getAvatar()).placeholder(R.mipmap.user).into(holder.avatar);


        return convertView;
    }
    static class ViewHolder {
        ImageView avatar;
        TextView nameView;
        TextView stateView;
    }
}
