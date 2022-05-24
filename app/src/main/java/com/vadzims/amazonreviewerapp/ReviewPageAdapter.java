package com.vadzims.amazonreviewerapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ReviewPageAdapter extends FragmentStateAdapter {
    /**
     * total tabs count
     */
    private int NUM_TABS = 7;

    public ReviewPageAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 1) {
            return new ReviewsOrderedFragment();
        } else if(position == 2) {
            return new ReviewsDeliveredFragment();
        } else if(position == 3) {
            return new ReviewsSentFragment();
        } else if(position == 4) {
            return new ReviewsGotFragment();
        } else if(position == 5) {
            return new ReviewsPaidFragment();
        } else if(position == 6) {
            return new ReviewsSoldFragment();
        } else {
            return new ReviewsAllFragment(); // return base/first tab
        }
    }

    @Override
    public int getItemCount() {
        return NUM_TABS;
    }
}
