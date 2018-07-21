package com.example.android.pingpongscorekeeper.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.android.pingpongscorekeeper.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}