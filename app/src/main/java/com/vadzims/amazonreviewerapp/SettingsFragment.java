package com.vadzims.amazonreviewerapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;


public class SettingsFragment extends Fragment {

    public SettingsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        TextInputEditText userName, userEmail;
        ShapeableImageView userImage = view.findViewById(R.id.userImage);

        userName = view.findViewById(R.id.userName);
        userEmail = view.findViewById(R.id.userEmail);

        if (Utils.getInstance(getContext()).getUsername() != null) {
            userName.setText(Utils.getInstance(getContext()).getUsername());
        }

        if (Utils.getInstance(getContext()).getUserEmail() != null) {
            userEmail.setText(Utils.getInstance(getContext()).getUserEmail());
        }

        if (Utils.getInstance(getContext()).getUserImage() != null) {
            Picasso.get().load(Utils.getInstance(getContext()).getUserImage()).into(userImage);
        }

        MaterialButton deleteAccountButton = view.findViewById(R.id.delete_account_button);

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("DELETE ACCOUNT BUTTON FRAGMENT IS CLICKED !!!!!!!!!!");
            }
        });

        return view;
    }
}