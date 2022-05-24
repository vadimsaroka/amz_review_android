package com.vadzims.amazonreviewerapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

public class ReviewsPaidFragment extends Fragment {
    RecyclerView recyclerView;
    JSONArray reviewsList;

    public ReviewsPaidFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reviews_paid, container, false);

        try {
            Utils.getInstance(getContext()).searchPaidReviews();
            recyclerView = view.findViewById(R.id.reviewsList);

            // use a linear layout manager
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            showPaidReviews();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    private void showPaidReviews() {
        try {
            reviewsList = Utils.getInstance(getContext()).getPaidReviews();
            RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> adapter = new RecycleViewAdapter(reviewsList, getContext());
            recyclerView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}