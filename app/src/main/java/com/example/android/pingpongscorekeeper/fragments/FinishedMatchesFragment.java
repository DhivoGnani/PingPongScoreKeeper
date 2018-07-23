package com.example.android.pingpongscorekeeper.fragments;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.pingpongscorekeeper.R;
import com.example.android.pingpongscorekeeper.activities.MatchActivity;
import com.example.android.pingpongscorekeeper.adapters.FinishedMatchesCursorAdapter;
import com.example.android.pingpongscorekeeper.data.PingPongContract;

import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.COLUMN_GAME_TIME_DONE_LOCAL_TITLE;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_NAME_TITLE;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_SETS_WON_TITLE;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_NAME_TITLE;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_SETS_WON_TITLE;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.CONTENT_URI;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.SORTED_GAME_TIME_DONE_LOCAL_DESC;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch._ID;

public class FinishedMatchesFragment extends Fragment implements  LoaderManager.LoaderCallbacks<Cursor>{

    private static final int PING_PONG_LOADER = 0;

    FinishedMatchesCursorAdapter finishedMatchesCursorAdapter;
    private ListView finishedMatchesListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_finished_matches, container, false);

        finishedMatchesListView = rootView.findViewById(R.id.list);

        finishedMatchesCursorAdapter = new FinishedMatchesCursorAdapter(getActivity(), null);
        finishedMatchesListView.setAdapter(finishedMatchesCursorAdapter);

        this.getLoaderManager().initLoader(PING_PONG_LOADER, null, this);

        View emptyView = rootView.findViewById(R.id.empty_view);
        finishedMatchesListView.setEmptyView(emptyView);

//        finishedMatchesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                Intent intent = new Intent(getActivity(), MatchActivity.class);
//                intent.setData(PingPongContract.Set.CONTENT_URI);
//                String matchId = id + "";
//                intent.putExtra("matchId", matchId);
//
//                startActivity(intent);
//            }
//        });

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
        finishedMatchesCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        finishedMatchesCursorAdapter.swapCursor(null);
    }
}
