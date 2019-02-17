package com.example.fsudouest.blablafit


import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil

import com.google.firebase.auth.FirebaseAuth

import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.fsudouest.blablafit.Ui.Activities.LoginActivity
import com.example.fsudouest.blablafit.Ui.Activities.NouvelleSeanceActivity
import com.example.fsudouest.blablafit.Util.BottomNavigationViewBehavior
import com.example.fsudouest.blablafit.Util.BottomNavigationViewHelper
import com.example.fsudouest.blablafit.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {


    private var mFirebaseAuth: FirebaseAuth? = null
    private var mAuthStateListener: FirebaseAuth.AuthStateListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        mFirebaseAuth = FirebaseAuth.getInstance()
        mAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        val navController = findNavController(this, R.id.myNavHostFragment)

        val navigation = binding.bottomNavigation
        BottomNavigationViewHelper.removeShiftMode(navigation)
        setupWithNavController(navigation, navController)

        val layoutParams = navigation.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.behavior = BottomNavigationViewBehavior()

        val floatingActionButton = binding.addWorkoutButton
        floatingActionButton.setOnClickListener { startActivity(Intent(this, NouvelleSeanceActivity::class.java)) }

    }

    override fun onResume() {
        super.onResume()
        mFirebaseAuth!!.addAuthStateListener(mAuthStateListener!!)
    }

    override fun onPause() {
        super.onPause()
        if (mAuthStateListener != null) {
            mFirebaseAuth!!.removeAuthStateListener(mAuthStateListener!!)
        }
    }

}