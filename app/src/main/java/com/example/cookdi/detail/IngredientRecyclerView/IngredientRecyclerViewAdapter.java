package com.example.cookdi.detail.IngredientRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookdi.R;

import java.util.ArrayList;

public class IngredientRecyclerViewAdapter extends RecyclerView.Adapter<IngredientRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<IngredientModel> models;

    public IngredientRecyclerViewAdapter(Context context, ArrayList<IngredientModel> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View ingredientView = inflater.inflate(R.layout.ingredient_recyclerview, parent, false);
        ViewHolder viewHolder = new ViewHolder(ingredientView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IngredientModel model = models.get(position);
        holder.contextTextView.setText(model.getContext());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView contextTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contextTextView = itemView.findViewById(R.id.txtIngredientContext);
        }
    }
}

