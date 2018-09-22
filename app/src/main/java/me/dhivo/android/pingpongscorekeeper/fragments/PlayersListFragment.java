package me.dhivo.android.pingpongscorekeeper.fragments;

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

import me.dhivo.android.pingpongscorekeeper.R;
import me.dhivo.android.pingpongscorekeeper.activities.PlayerEditorActivity;
import me.dhivo.android.pingpongscorekeeper.adapters.PlayersAdapter;
import me.dhivo.android.pingpongscorekeeper.data.PingPongContract;

public class PlayersListFragment extends Fragment implements  LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PING_PONG_LOADER = 0;

    PlayersAdapter playersAdapter;
    private ListView playersListView;

    public void refreshList()
    {
        playersAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.players_list, container, false);

        playersListView = rootView.findViewById(R.id.player_list);

        playersAdapter = new PlayersAdapter(getActivity(), null);

        playersListView.setAdapter(playersAdapter);

        this.getLoaderManager().initLoader(PING_PONG_LOADER, null, this);

        rootView.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PlayerEditorActivity.class);
                startActivity(intent);
            }
        });

        playersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PlayerEditorActivity.class);

                Uri currentPetUri = ContentUris.withAppendedId(PingPongContract.Player.CONTENT_URI, id);

                intent.setData(currentPetUri);

                startActivity(intent);
            }
        });
        return rootView;
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {

        return new CursorLoader(getActivity(), PingPongContract.Player.CONTENT_URI, null, null, null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        playersAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        playersAdapter.swapCursor(null);
    }
}
