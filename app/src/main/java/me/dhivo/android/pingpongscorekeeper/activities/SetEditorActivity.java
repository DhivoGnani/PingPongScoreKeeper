package me.dhivo.android.pingpongscorekeeper.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import me.dhivo.android.pingpongscorekeeper.R;
import me.dhivo.android.pingpongscorekeeper.adapters.SetAdapter;
import me.dhivo.android.pingpongscorekeeper.components.PingPongSet;
import me.dhivo.android.pingpongscorekeeper.data.PingPongContract;
import me.dhivo.android.pingpongscorekeeper.validators.SetValidator;

import java.util.ArrayList;

public class SetEditorActivity extends AppCompatActivity {

    private ArrayList<PingPongSet> sets;
    private long playerOneId;
    private long playerTwoId;
    private long servingPlayerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_editor);

        String playerOneName = getIntent().getExtras().getString("playerOneName");
        String playerTwoName = getIntent().getExtras().getString("playerTwoName");
        playerOneId = getIntent().getExtras().getLong("playerOneId");
        playerTwoId = getIntent().getExtras().getLong("playerTwoId");
        int numOfSets = Integer.valueOf(getIntent().getExtras().getString("numSets"));
        final String servingPlayer = getIntent().getExtras().getString("servingPlayer");

        servingPlayerId = "Player 1".equals(servingPlayer) ? playerOneId : playerTwoId;

        TextView p1 = findViewById(R.id.player_one_id);
        p1.setText(playerOneName);

        TextView p2 = findViewById(R.id.player_two_id);
        p2.setText(playerTwoName);

        sets = createTempPingPongSets(numOfSets);

        ListView setList = findViewById(R.id.setlist);

        SetAdapter adapter = new SetAdapter(this, 0, sets );

        setList.setAdapter(adapter);
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

    private void insertFinishedMatch(long playerOneId, long playerTwoId, int numberOfSetsPlayerOneWon, int numberOfSetsPlayerTwoWon, ArrayList<PingPongSet> pingPongSets)
    {
        ContentValues values = new ContentValues();
        values.put(PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_ID_TITLE, playerOneId);
        values.put(PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_ID_TITLE,  playerTwoId);
        values.put(PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_SETS_WON_TITLE,  numberOfSetsPlayerOneWon);
        values.put(PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_SETS_WON_TITLE, numberOfSetsPlayerTwoWon);
        values.put(PingPongContract.PingPongMatch.COLUMN_SERVING_PLAYER_ID_TITLE, servingPlayerId);

        Uri uri =getContentResolver().insert(PingPongContract.PingPongMatch.CONTENT_URI, values);

        long id = Long.valueOf(uri.getLastPathSegment());

        for(PingPongSet set: pingPongSets)
        {
            values = new ContentValues();
            values.put(PingPongContract.Set.MATCH_ID, id+ "");
            values.put(PingPongContract.Set.SET_NUMBER, set.getSetNumber());
            values.put(PingPongContract.Set.PLAYER_ONE_SCORE,  set.getPlayerOneScore());
            values.put(PingPongContract.Set.PLAYER_TWO_SCORE, set.getPlayerTwoScore());

            getContentResolver().insert(PingPongContract.Set.CONTENT_URI, values);
        }
    }

    public boolean done()
    {
        int numOfPlayerOneWon = 0;
        int numOfPlayerTwoWon = 0;

        for(int i = 0; i < sets.size(); i++ ) {
            if(sets.get(i).getPlayerOneScore() == -1 || sets.get(i).getPlayerTwoScore() == -1)
            {
                Toast.makeText(this, "Add set values",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            if(sets.get(i).getPlayerOneScore() > sets.get(i).getPlayerTwoScore())
            {
                numOfPlayerOneWon++;
            } else {
                numOfPlayerTwoWon++;
            }
         }

        boolean isSetsValid = SetValidator.validateSets(sets, 11);
        if(!isSetsValid) {
            Toast.makeText(this, "Set points are invalid",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        insertFinishedMatch(playerOneId, playerTwoId, numOfPlayerOneWon, numOfPlayerTwoWon, sets);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_player_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if(item.getItemId()  == R.id.action_close)  {
            finish();
            return true;
        } else if(item.getItemId() == R.id.action_done) {
            if(done()) {
                finish();
                return true;
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}