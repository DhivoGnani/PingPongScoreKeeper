package com.example.android.pingpongscorekeeper.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android.pingpongscorekeeper.R;
import com.example.android.pingpongscorekeeper.adapters.FinishedMatchesAdapter;
import com.example.android.pingpongscorekeeper.data.PingPongContract;

import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.COLUMN_GAME_TIME_DONE_LOCAL_TITLE;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.COLUMN_GAME_TIME_DONE_TITLE;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_NAME_TITLE;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_SETS_WON_TITLE;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_NAME_TITLE;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_SETS_WON_TITLE;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.CONTENT_URI;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.SORTED_GAME_TIME_DONE_LOCAL_DESC;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch._ID;

public class FinishedMatchesFragment extends Fragment implements  LoaderManager.LoaderCallbacks<Cursor>{

    private static final int PING_PONG_LOADER = 0;

    FinishedMatchesAdapter mCursorAdapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_finished_matches, container, false);



        listView = (ListView) rootView.findViewById(R.id.list);

        mCursorAdapter = new FinishedMatchesAdapter(getActivity(), null);
        listView.setAdapter(mCursorAdapter);
        this.getLoaderManager().initLoader(PING_PONG_LOADER, null, this);
        return rootView;
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        String[] projection = {_ID, COLUMN_PLAYER_ONE_NAME_TITLE, COLUMN_PLAYER_TWO_NAME_TITLE,
                COLUMN_PLAYER_ONE_SETS_WON_TITLE, COLUMN_PLAYER_TWO_SETS_WON_TITLE,
                COLUMN_GAME_TIME_DONE_LOCAL_TITLE};

        return new CursorLoader(getActivity(), CONTENT_URI, projection, null, null,
                SORTED_GAME_TIME_DONE_LOCAL_DESC);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
//        listView.setSelection(mCursorAdapter.getCount() - 1);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);

    }
}
