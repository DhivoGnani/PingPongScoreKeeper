package com.example.android.pingpongscorekeeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.android.pingpongscorekeeper.adapters.FinishedMatchesAdapter;

import java.util.ArrayList;

public class FinishedMatchesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_matches);


        // Create an ArrayList of AndroidFlavor objects
        ArrayList<String> androidFlavors = new ArrayList<String>();
        androidFlavors.add("");
        androidFlavors.add("");
        androidFlavors.add("");
        androidFlavors.add("");
        androidFlavors.add("");
        androidFlavors.add("");
        androidFlavors.add("");
        androidFlavors.add("");
        androidFlavors.add("");
        androidFlavors.add("");
        androidFlavors.add("");
        androidFlavors.add("");
        androidFlavors.add("");
        androidFlavors.add("");
        androidFlavors.add("");
        androidFlavors.add("");
        androidFlavors.add("");
        androidFlavors.add("");
        androidFlavors.add("");
        androidFlavors.add("");
        androidFlavors.add("");
        androidFlavors.add("");
        androidFlavors.add("");
        androidFlavors.add("");
        androidFlavors.add("");
        androidFlavors.add("");
        androidFlavors.add("");
        androidFlavors.add("");

        FinishedMatchesAdapter flavorAdapter = new FinishedMatchesAdapter(this, androidFlavors);

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(flavorAdapter);
    }
}
