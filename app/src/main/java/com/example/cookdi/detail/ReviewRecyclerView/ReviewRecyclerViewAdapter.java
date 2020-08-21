package com.example.cookdi.detail.ReviewRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookdi.R;

import java.util.ArrayList;


public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ReviewModel> models;
    private final int ITEMS_LIMIT = 10;

    public ReviewRecyclerViewAdapter(Context context, ArrayList<ReviewModel> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View stepView = inflater.inflate(R.layout.review_recyclerview, parent, false);
        ViewHolder viewHolder = new ViewHolder(stepView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReviewModel model = models.get(position);

        holder.usernameTextView.setText(model.getUsername());
        holder.reviewTextView.setText(model.getContext());
        holder.ratingBar.setRating(model.getRating());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView usernameTextView;
        private TextView reviewTextView;
        private RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.txtUsernameReviewRecyclerView);
            reviewTextView = itemView.findViewById(R.id.txtReviewReviewRecyclerView);
            ratingBar = itemView.findViewById(R.id.barRatingFoodReviewRecyclerView);
        }
    }

    public int getItemLimit(){
        return this.ITEMS_LIMIT;
    }
}

