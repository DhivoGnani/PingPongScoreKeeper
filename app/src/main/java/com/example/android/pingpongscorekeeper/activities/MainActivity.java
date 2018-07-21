package com.example.android.pingpongscorekeeper.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.pingpongscorekeeper.R;
import com.example.android.pingpongscorekeeper.adapters.CategoryAdapter;
import com.example.android.pingpongscorekeeper.data.PingPongContract;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        CategoryAdapter adapter = new CategoryAdapter(this, getSupportFragmentManager());

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);

        String id = getIntent().getStringExtra("cool");

        if(id != null) {
            tabLayout.getTabAt(1).select();
//            ((ListView)findViewById(R.id.list)).setSelection(adapter.getCount() - 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        menu.add(0, 0, 0, "Delete All");
        menu.add(0, 1, 0, "Insert Dummy Data");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                deleteAllMatches();
                break;
            case 1:
                insertDummyMatchData();
                break;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllMatches() {
        getContentResolver().delete(PingPongContract.PingPongMatch.CONTENT_URI, null, null);
    }

    private void insertDummyMatchData()
    {
        ContentValues values = new ContentValues();
        values.put(PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_NAME_TITLE, "Dhivo");
        values.put(PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_NAME_TITLE, "Michael");
        values.put(PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_SETS_WON_TITLE, 3);
        values.put(PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_SETS_WON_TITLE, 4);
        getContentResolver().insert(PingPongContract.PingPongMatch.CONTENT_URI, values);
    }

}
