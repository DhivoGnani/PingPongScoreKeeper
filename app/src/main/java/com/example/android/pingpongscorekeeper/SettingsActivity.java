package com.example.android.pingpongscorekeeper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

//TODO: Why is AppCompactActivity and not Activity used here?
public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
