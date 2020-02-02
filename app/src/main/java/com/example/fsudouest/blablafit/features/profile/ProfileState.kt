package com.example.fsudouest.blablafit.features.profile

import com.example.fsudouest.blablafit.features.profile.buddies.BuddyViewItem
import com.example.fsudouest.blablafit.model.User

sealed class ProfileState {
    abstract val data: ProfileData

    data class Idle(override val data: ProfileData): ProfileState()
    data class UserLoaded(override val data: ProfileData): ProfileState()
    data class BuddiesLoaded(override val data: ProfileData): ProfileState()
    data class EmptyBuddies(override val data: ProfileData): ProfileState()
}

data class ProfileData(
        val user: User? = null,
        val buddies: List<BuddyViewItem> = emptyList()
)
