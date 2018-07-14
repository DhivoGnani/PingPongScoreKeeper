package com.example.android.pingpongscorekeeper.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.android.pingpongscorekeeper.R;

import java.util.ArrayList;

public class FinishedMatchesAdapter  extends ArrayAdapter<String>
{

    public FinishedMatchesAdapter(Activity context, ArrayList<String> finishedGames)
    {
        super(context, 0, finishedGames);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        return listItemView;
    }
}
