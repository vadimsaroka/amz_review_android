package com.vadzims.amazonreviewerapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    private final JSONArray reviews;
    Context context;

    public RecycleViewAdapter(JSONArray reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    @NonNull
    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_row, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter.ViewHolder holder, int position) {
        try {
            JSONObject object = reviews.getJSONObject(position);
            Picasso.get().load(object.getString("imageCover")).into(holder.reviewPicture);
            holder.reviewDate.setText(object.getString("orderDate"));
            holder.reviewDescription.setText(object.getString("name"));
            holder.reviewPrice.setText(context.getString(R.string.price_formatted, object.getString("price")));
            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(context, ReviewEditActivity.class);
                        intent.putExtra("REVIEW_ID", object.getString("_id"));
                        System.out.println("RESPONSE FROM THE ADAPTER: " + object.getString("_id"));
                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    @Override
    public int getItemCount() {
        return reviews.length();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView reviewPicture;
        TextView reviewPrice, reviewDescription, reviewDate;
        MaterialCardView parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            reviewPicture = itemView.findViewById(R.id.reviewPicture);
            reviewPrice = itemView.findViewById(R.id.reviewPrice);
            reviewDescription = itemView.findViewById(R.id.reviewDescription);
            reviewDate = itemView.findViewById(R.id.reviewDate);
            parentLayout = itemView.findViewById(R.id.reviewCard);
        }
    }
}
