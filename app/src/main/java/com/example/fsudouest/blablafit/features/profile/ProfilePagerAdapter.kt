package com.example.fsudouest.blablafit.features.profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fsudouest.blablafit.features.profile.buddies.WorkoutBuddiesFragment
import com.example.fsudouest.blablafit.features.profile.personalnfo.PersonalInfoFragment

const val NUM_PAGES = 2

class ProfilePagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount() = NUM_PAGES

    override fun createFragment(position: Int): Fragment =
            when (position) {
                0 -> PersonalInfoFragment()
                1 -> WorkoutBuddiesFragment()
                else -> throw IllegalStateException()
            }
}