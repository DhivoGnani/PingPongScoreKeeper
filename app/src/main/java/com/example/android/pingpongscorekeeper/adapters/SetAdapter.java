package com.example.android.pingpongscorekeeper.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.example.android.pingpongscorekeeper.R;
import com.example.android.pingpongscorekeeper.components.PingPongSet;

import java.util.List;

public class SetAdapter extends ArrayAdapter<PingPongSet> {
    private Context context;

    public SetAdapter(@NonNull Context context, int resource, @NonNull List<PingPongSet> objects) {
        super(context, resource, objects);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PingPongSet set = getItem(position);

        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.editable_set_item, parent, false);
        }

        EditText playerOneScore = convertView.findViewById(R.id.player_one_num);
        EditText playerTwoScore = convertView.findViewById(R.id.player_two_num);
        EditText title = convertView.findViewById(R.id.set_n);

        title.setText("SET " + set.getSetNumber());

        if(set.getPlayerOneScore() >= 0) {
            playerOneScore.setText(String.valueOf(set.getPlayerOneScore()));
        } else {
            playerOneScore.setText("");
        }

        if(set.getPlayerTwoScore() >= 0) {
            playerTwoScore.setText(String.valueOf(set.getPlayerTwoScore()));
        } else {
            playerTwoScore.setText("");
        }

        return convertView;
    }

}
