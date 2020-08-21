package com.example.cookdi.detail.StepRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookdi.R;

import java.util.ArrayList;

public class StepRecyclerViewAdapter extends RecyclerView.Adapter<StepRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<StepModel> models;

    public StepRecyclerViewAdapter(Context context, ArrayList<StepModel> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View stepView = inflater.inflate(R.layout.step_recyclerview, parent, false);
        ViewHolder viewHolder = new ViewHolder(stepView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StepModel model = models.get(position);

        holder.numberTextView.setText(Integer.toString(model.getNumber()));
        holder.contextTextView.setText(model.getContext());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView numberTextView;
        private TextView contextTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            numberTextView = itemView.findViewById(R.id.txtStepNumber);
            contextTextView = itemView.findViewById(R.id.txtStepContext);
        }
    }
}

