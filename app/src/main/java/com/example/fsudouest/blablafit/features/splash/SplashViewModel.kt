package com.example.fsudouest.blablafit.features.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.model.User
import com.google.firebase.firestore.FirebaseFirestore

class SplashViewModel @ViewModelInject constructor(private val firestore: FirebaseFirestore) : ViewModel() {

    private val stateLiveData = MutableLiveData<SplashState>()

    init {
        stateLiveData.value = SplashState.Loading
    }

    fun stateLiveData(): LiveData<SplashState> = stateLiveData

    fun userIsSetup(uid: String) {
        firestore.collection("users").document(uid)
                .get()
                .addOnSuccessListener {
                    val user = it.toObject(User::class.java)
                    stateLiveData.value = SplashState.UserLoaded(user?.setup ?: false)
                }
                .addOnFailureListener { stateLiveData.value = SplashState.UserLoaded(null) }
    }
}