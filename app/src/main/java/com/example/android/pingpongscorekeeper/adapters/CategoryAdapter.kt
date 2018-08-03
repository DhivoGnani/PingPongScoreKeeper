package com.example.android.pingpongscorekeeper.adapters


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import com.example.android.pingpongscorekeeper.fragments.FinishedMatchesFragment
import com.example.android.pingpongscorekeeper.fragments.NewGameFragment
import com.example.android.pingpongscorekeeper.fragments.PlayersListFragment

class CategoryAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> NewGameFragment()
            1 -> FinishedMatchesFragment()
            2 ->  PlayersListFragment()
            else -> throw IllegalStateException("Unknown position $position")
        }
    }

    override fun getCount(): Int  = 3

    override fun getPageTitle(position: Int): String {
        return when (position) {
            0 -> "New Game"
            1 -> "Finished"
            2 -> "Players"
            else -> throw IllegalStateException("Unknown position $position")
        }
    }
}