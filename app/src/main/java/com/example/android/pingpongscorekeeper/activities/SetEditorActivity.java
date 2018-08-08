package com.example.android.pingpongscorekeeper.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.pingpongscorekeeper.R;
import com.example.android.pingpongscorekeeper.components.PingPongSet;

import java.util.ArrayList;

public class SetEditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_editor);

    }

    private ArrayList<PingPongSet> createTempPingPongSets(int numOfSets)
    {
        ArrayList<PingPongSet> pingPongSets = new ArrayList<>();

        for(int i = 0; i < numOfSets; i++)
        {
            pingPongSets.add(new PingPongSet(i + 1, -1, -1));
        }

        return pingPongSets;
    }
}
