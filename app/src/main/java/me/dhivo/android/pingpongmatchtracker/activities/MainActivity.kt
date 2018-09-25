package me.dhivo.android.pingpongmatchtracker.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import me.dhivo.android.pingpongmatchtracker.R
import me.dhivo.android.pingpongmatchtracker.adapters.CategoryAdapter


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val viewPager = findViewById<ViewPager>(R.id.viewpager)

        val adapter = CategoryAdapter(supportFragmentManager)

        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 2

        val tabLayout = findViewById<TabLayout>(R.id.tabs)

        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.mainmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
