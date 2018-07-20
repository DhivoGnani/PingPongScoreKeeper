package com.example.android.pingpongscorekeeper.activities;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android.pingpongscorekeeper.R;
import com.example.android.pingpongscorekeeper.adapters.FinishedMatchesAdapter;

import java.util.ArrayList;

public class FinishedMatchesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_finished_matches, container, false);


        // Create an ArrayList of AndroidFlavor objects
        ArrayList<String> androidFlavors = new ArrayList<String>();
        androidFlavors.add("Dhivo");
        androidFlavors.add("Great");
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

        FinishedMatchesAdapter flavorAdapter = new FinishedMatchesAdapter(getActivity(), androidFlavors);

        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(flavorAdapter);
        return rootView;
    }
}
