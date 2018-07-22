package com.example.android.pingpongscorekeeper.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.android.pingpongscorekeeper.R;
import com.example.android.pingpongscorekeeper.adapters.MatchSetsAdapter;
import com.example.android.pingpongscorekeeper.data.PingPongContract;

import static com.example.android.pingpongscorekeeper.data.PingPongContract.Set.SORTED_SETS;

public class MatchActivity extends AppCompatActivity implements  LoaderCallbacks<Cursor> {

    private static final int SET_LOADER = 0;
    private Uri mCurrentUri;
    private MatchSetsAdapter adapter;
    private int matchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        Intent intent = getIntent();
        mCurrentUri = intent.getData();

        matchId =    Integer.valueOf(getIntent().getExtras().getString("matchId"));

        ListView list =  findViewById(R.id.setlist);
        adapter = new MatchSetsAdapter(this , null);

        list.setAdapter(adapter);

        this.getSupportLoaderManager().initLoader(SET_LOADER, null, this);
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            String selection = PingPongContract.Set.MATCH_ID + "=?";
            String[] selectionArgs = new String[] { String.valueOf(matchId) };

        return new CursorLoader(this, mCurrentUri, null, selection, selectionArgs,
                SORTED_SETS);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_close:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
