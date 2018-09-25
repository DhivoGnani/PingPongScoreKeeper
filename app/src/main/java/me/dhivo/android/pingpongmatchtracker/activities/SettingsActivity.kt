package me.dhivo.android.pingpongmatchtracker.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

import me.dhivo.android.pingpongmatchtracker.fragments.SettingsFragment

//TODO: Why is AppCompactActivity and not Activity used here?
class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        fragmentManager.beginTransaction()
                .replace(android.R.id.content, SettingsFragment())
                .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
