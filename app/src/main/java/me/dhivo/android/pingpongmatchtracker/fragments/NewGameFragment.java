package me.dhivo.android.pingpongmatchtracker.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import me.dhivo.android.pingpongmatchtracker.R;
import me.dhivo.android.pingpongmatchtracker.activities.ScoreKeeperActivity;
import me.dhivo.android.pingpongmatchtracker.activities.SetEditorActivity;
import me.dhivo.android.pingpongmatchtracker.components.GameConfiguration;
import me.dhivo.android.pingpongmatchtracker.customviews.NoDefaultSpinner;
import me.dhivo.android.pingpongmatchtracker.data.PingPongContract;

public class NewGameFragment extends Fragment implements  LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemSelectedListener
{
    private static final int PING_PONG_LOADER = 0;
    private GameConfiguration configuration;

    private NoDefaultSpinner playerOneDisplay;
    private NoDefaultSpinner playerTwoDisplay;
    private CheckBox playerOneServe;
    private CheckBox playerTwoServe;

    private long playerOneId;
    private long playerTwoId;
    private String playerOneName;
    private String playerTwoName;

    private View rootView;

    Integer playerOneDisplayPosition;
    Integer playerTwoDisplayPosition;

    private SimpleCursorAdapter madapter;
    private SimpleCursorAdapter madapter2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_new_game, container, false);

        configuration = new GameConfiguration();
        playerOneDisplay = rootView.findViewById(R.id.player_one);
        playerTwoDisplay = rootView.findViewById(R.id.player_two);

        playerOneServe = rootView.findViewById(R.id.player_one_serve);
        playerTwoServe = rootView.findViewById(R.id.player_two_serve);

        ((View)playerOneServe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player_one_check_box(view);
            }
        });

        ((View)playerTwoServe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player_two_check_box(view);
            }
        });

        rootView.findViewById(R.id.increase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementSets(view);
            }
        });

        rootView.findViewById(R.id.decrease).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrementSets(view);
            }
        });

        rootView.findViewById(R.id.increase_set_points).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementSetPoints(view);
            }
        });

        rootView.findViewById(R.id.decrease_set_points).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                decrementSetPoints(view);
            }
        });
        rootView.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGameOnClick(view, false);
            }
        });

        rootView.findViewById(R.id.start_game).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGameOnClick(view, true);
            }
        });

        String[] fromColumns = {
                PingPongContract.Player.COLUMN_NAME_TITLE
        };
        int[] toViews = {
                android.R.id.text1
        };

        madapter = new SimpleCursorAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                null,
                fromColumns,
                toViews,
                0);

        madapter2 = new SimpleCursorAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                null,
                fromColumns,
                toViews,
                0);

        madapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        madapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        playerOneDisplay.setAdapter(madapter);
        playerTwoDisplay.setAdapter(madapter2);

        this.getLoaderManager().initLoader(PING_PONG_LOADER, null, this);

        playerTwoDisplay.setOnItemSelectedListener(this);
        playerOneDisplay.setOnItemSelectedListener(this);
        return rootView;
    }

    private void decrementSetPoints(View view) {
        configuration.decrementNumberOfPointsPerSet();
        displayNumberOfPointsPerSet();
    }

    private void incrementSetPoints(View view) {
        configuration.incrementNumberOfPointersPerSet();
        displayNumberOfPointsPerSet();
    }

    public void decrementSets(View view) {
        configuration.decrementNumberOfSets();
        displayNumberOfSets();
    }

    public void incrementSets(View view) {
        configuration.incrementNumberOfSets();
        displayNumberOfSets();
    }

    public void displayNumberOfSets()
    {
        ((TextView)rootView.findViewById(R.id.num_of_sets)).
                setText(Integer.toString(configuration.getNumberOfSets()));
    }

    public void displayNumberOfPointsPerSet()
    {
        ((TextView)rootView.findViewById(R.id.num_of_sets_points)).
                setText(Integer.toString(configuration.getNumberOfPointsPerSet()));
    }

    public void startGameOnClick(View view, boolean start)
    {

        if(!playersChosenAreValid()) return;

        String numSets = configuration.getNumberOfSets() + "";
        String servingPlayer = configuration.getServingPlayer().toString();

        // TODO: Find better way to pass necessary data to GameActivity
        Intent intent = start? new Intent(getActivity(), ScoreKeeperActivity.class) :
                new Intent(getActivity(), SetEditorActivity.class) ;
        intent.putExtra("playerOneName", playerOneName);
        intent.putExtra("playerTwoName", playerTwoName);
        intent.putExtra("playerOneId", playerOneId);
        intent.putExtra("playerTwoId", playerTwoId);
        intent.putExtra("numSets", numSets);
        intent.putExtra("numPointsPerSet", configuration.getNumberOfPointsPerSet() + "");
        intent.putExtra("servingPlayer", servingPlayer);
        startActivity(intent);
    }

    // TODO: remove duplicate code
    public void player_two_check_box(View view) {
        configuration.switchServingPlayer();
        if(playerTwoServe.isChecked())
        {
           if(playerOneServe.isChecked()) playerOneServe.toggle();
        }
        else
        {
            if(!playerOneServe.isChecked()) playerOneServe.toggle();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(playerOneDisplayPosition == null) return;
        playerOneDisplay.setSelection(playerOneDisplayPosition);
        playerTwoDisplay.setSelection(playerTwoDisplayPosition);
    }

    // TODO: remove duplicate code
    public void player_one_check_box(View view) {
        configuration.switchServingPlayer();
        if(playerOneServe.isChecked())
        {
            if(playerTwoServe.isChecked())  playerTwoServe.toggle();
        }
        else
        {
            if(!playerTwoServe.isChecked()) playerTwoServe.toggle();
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {

        return new CursorLoader(getActivity(), PingPongContract.Player.CONTENT_URI, null, null, null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        madapter.swapCursor(cursor);
        madapter2.swapCursor(cursor);

        // TODO: Fix this
        playerOneDisplay.setAdapter(madapter);
        playerTwoDisplay.setAdapter(madapter2);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        madapter.swapCursor(null);
        madapter2.swapCursor(null);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Cursor x = (Cursor)adapterView.getItemAtPosition(i);
            String f  = x.getString(x.getColumnIndex(PingPongContract.Player.COLUMN_NAME_TITLE));
            long id = x.getLong(x.getColumnIndex(PingPongContract.Player._ID));
            if(adapterView == playerOneDisplay) {
                playerOneName = f;
                playerOneId = id;
            } else {
                playerTwoName = f;
                playerTwoId = id;
            }
        playerOneDisplayPosition = playerOneDisplay.getSelectedItemPosition();
        playerTwoDisplayPosition = playerTwoDisplay.getSelectedItemPosition();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public boolean playersChosenAreValid()
    {
        if(madapter.isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.add_players), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(playerOneName == null || playerTwoName == null)  {
            Toast.makeText(getActivity(), getString(R.string.choose_players), Toast.LENGTH_SHORT).show();
            return false;
        }

        if(playerOneId == playerTwoId) {
            Toast.makeText(getActivity(), getString(R.string.same_players_chosen),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
