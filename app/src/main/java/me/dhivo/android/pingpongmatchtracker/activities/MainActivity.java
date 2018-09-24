package me.dhivo.android.pingpongmatchtracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import me.dhivo.android.pingpongmatchtracker.R;
import me.dhivo.android.pingpongmatchtracker.adapters.CategoryAdapter;
import me.dhivo.android.pingpongmatchtracker.data.PingPongContract;
import me.dhivo.android.pingpongmatchtracker.helpers.DummyDataHelper;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ViewPager viewPager = findViewById(R.id.viewpager);

        CategoryAdapter adapter = new CategoryAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        menu.add(0, 0, 0, "Delete All");
        menu.add(0, 1, 0, "Insert Match Dummy Data");
        menu.add(0, 2, 0, "Insert Player Dummy Data");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                deleteAllMatches();
                break;
            case 1:
                DummyDataHelper.insertDummyMatchData(getContentResolver());
                break;
            case 2:
                DummyDataHelper.insertDummyPlayer("Neymar", getContentResolver());
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
}
