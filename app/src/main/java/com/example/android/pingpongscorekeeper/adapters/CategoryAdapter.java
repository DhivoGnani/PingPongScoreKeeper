package com.example.android.pingpongscorekeeper.adapters;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.pingpongscorekeeper.activities.FinishedMatchesFragment;
import com.example.android.pingpongscorekeeper.fragments.NewGameFragment;

public class CategoryAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public CategoryAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        if(position == 0) return new NewGameFragment();
        else return new FinishedMatchesFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0) return "New Game";
        else return "Finished";
    }
}