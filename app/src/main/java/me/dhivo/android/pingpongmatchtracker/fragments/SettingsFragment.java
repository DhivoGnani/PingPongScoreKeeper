package me.dhivo.android.pingpongmatchtracker.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import me.dhivo.android.pingpongmatchtracker.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}