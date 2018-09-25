package me.dhivo.android.pingpongmatchtracker.fragments

import android.os.Bundle
import android.preference.PreferenceFragment

import me.dhivo.android.pingpongmatchtracker.R

class SettingsFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)
    }
}