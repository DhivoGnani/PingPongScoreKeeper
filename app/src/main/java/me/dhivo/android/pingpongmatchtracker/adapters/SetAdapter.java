package me.dhivo.android.pingpongmatchtracker.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;


import me.dhivo.android.pingpongmatchtracker.R;
import me.dhivo.android.pingpongmatchtracker.components.PingPongSet;

import java.util.List;

public class SetAdapter extends ArrayAdapter<PingPongSet> {
    private final List<PingPongSet> sets;
    private Context context;

    public SetAdapter(@NonNull Context context, int resource, @NonNull List<PingPongSet> objects) {
        super(context, resource, objects);
        this.context = context;
        sets = objects;
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
        TextView title = convertView.findViewById(R.id.set_n);

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

        final PingPongSet currentSet = sets.get(position);

        playerOneScore.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                try {

                    currentSet.setPlayerOneScore(Integer.valueOf(s.toString()));
                }
                catch(Exception ex)
                {

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });

        playerTwoScore.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                try {

                    currentSet.setPlayerTwoScore(Integer.valueOf(s.toString()));
                }
                catch(Exception ex)
                {

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });

        return convertView;
    }

}
