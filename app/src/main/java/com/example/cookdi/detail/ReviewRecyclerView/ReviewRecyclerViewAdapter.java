package com.example.cookdi.detail.ReviewRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cookdi.R;
import com.example.cookdi.retrofit2.entities.Review;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Review> models;
    private final int ITEMS_LIMIT = 10;

    public ReviewRecyclerViewAdapter(Context context, List<Review> models) {
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
        Review model = models.get(position);

        holder.usernameTextView.setText(model.getCommentator().getUser_name());
        holder.reviewTextView.setText(model.getComment().getComment_Content());
        holder.ratingBar.setRating(model.getComment().getRate());
        Picasso.get().load(model.getCommentator().getUser_avatar_url()).error(R.drawable.ic_error).placeholder(R.drawable.ic_placeholder_background).into(holder.avatarImageView);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView usernameTextView;
        private TextView reviewTextView;
        private RatingBar ratingBar;
        private ImageView avatarImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.textViewUsernameReviewRecyclerView);
            reviewTextView = itemView.findViewById(R.id.textViewReviewReviewRecyclerView);
            ratingBar = itemView.findViewById(R.id.barRatingFoodReviewRecyclerView);
            avatarImageView = itemView.findViewById(R.id.imageViewUserAvatarReviewRecyclerView);
        }
    }

    public int getItemLimit(){
        return this.ITEMS_LIMIT;
    }
}

