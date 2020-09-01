package com.example.cookdi.detail.StepRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookdi.R;
import com.example.cookdi.retrofit2.entities.RecipeStep;
import com.example.cookdi.retrofit2.entities.SavedRecipeStep;

import java.util.ArrayList;
import java.util.List;

public class StepRecyclerViewAdapter extends RecyclerView.Adapter<StepRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<RecipeStep> models = null;
    private ArrayList<SavedRecipeStep> savedRecipeSteps = null;

    public StepRecyclerViewAdapter(Context context, List<RecipeStep> models) {
        this.context = context;
        this.models = models;
    }

    // for offline mode
    public StepRecyclerViewAdapter(Context context, ArrayList<SavedRecipeStep> models) {
        this.context = context;
        this.savedRecipeSteps = models;
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
        RecipeStep model = new RecipeStep();
        if(models != null) {
            model = models.get(position);
        }
        else {
            model = savedRecipeSteps.get(position);
        }

        holder.orderTextView.setText(Integer.toString(model.getStep_Order()));
        holder.descriptionTextView.setText(model.getStep_description());
        String time = String.format("%02d:%02d",  model.getDuration_minute() / 60, model.getDuration_minute() % 60);
        holder.timeTextView.setText(time);
    }

    @Override
    public int getItemCount() {
        return models != null ? models.size() : savedRecipeSteps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView orderTextView;
        private TextView descriptionTextView;
        private TextView timeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderTextView = itemView.findViewById(R.id.textViewStepOrderStepRecyclerView);
            descriptionTextView = itemView.findViewById(R.id.textViewStepDescriptionStepRecyclerView);
            timeTextView = itemView.findViewById(R.id.textViewTimeStepRecyclerView);
        }
    }
}

