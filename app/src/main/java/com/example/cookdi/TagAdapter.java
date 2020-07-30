package com.example.cookdi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class TagAdapter extends BaseAdapter {
    private List<String> TagName;
    private LayoutInflater layoutInflater;
    private Context context;

    public TagAdapter(Context aContext,  List<String> TagData) {
        this.context = aContext;
        this.TagName = TagData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return TagName.size();
    }

    @Override
    public Object getItem(int position) {
        return TagName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.tag_detail, null);
            holder = new ViewHolder();
            holder.Tagname = (TextView) convertView.findViewById(R.id.tagname);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }


    static class ViewHolder {
        TextView Tagname;
    }

}
