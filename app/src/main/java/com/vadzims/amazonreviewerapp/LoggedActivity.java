package com.vadzims.amazonreviewerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigationrail.NavigationRailView;

public class LoggedActivity extends AppCompatActivity implements NavigationHost {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);

        NavigationRailView navigationRail = findViewById(R.id.navigation_rail);

        navigateTo(new SettingsFragment(), false); // Init view

        navigationRail.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String title = item.getTitle().toString();
                switch (title) {
                    case "New":
                        navigateTo(new NewReviewFragment(), false); // Navigate to the next Fragment
                        return true;
                    case "Reviews":
                        navigateTo(new ReviewsFragment(), false); // Navigate to the next Fragment
                        return true;
                    case "Search":
                        navigateTo(new SearchFragment(), false); // Navigate to the next Fragment
                        return true;
                    case "Settings":
                        navigateTo(new SettingsFragment(), false); // Navigate to the next Fragment
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // intentionally do nothing
    }

    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}