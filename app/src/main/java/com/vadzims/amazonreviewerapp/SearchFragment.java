package com.vadzims.amazonreviewerapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SearchFragment extends Fragment {
    RecyclerView recyclerView;
    SearchView searchView;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchView = view.findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Utils.getInstance(getContext()).hideKeyBoard(view);
                try {
                    Utils.getInstance(getContext()).searchReviewsByName(query, new Utils.VolleyResponseListener() {
                        @Override
                        public void onError(String message) {
                            VolleyLog.e("Something went wrong");
                        }

                        @Override
                        public void onResponse(JSONObject response) throws JSONException {
                            recyclerView = view.findViewById(R.id.reviewsList);
                            showAllReviewsByName(response.getJSONArray("data"));
                        }
                    });
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }

    private void showAllReviewsByName(JSONArray response) {
        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> adapter = new RecycleViewAdapter(response, getContext());
        recyclerView.setAdapter(adapter);
    }
}