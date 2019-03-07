package com.example.fsudouest.blablafit


import android.content.Intent
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment

import com.google.firebase.auth.FirebaseAuth

import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.fsudouest.blablafit.Ui.Activities.LoginActivity
import com.example.fsudouest.blablafit.Ui.Fragments.NouvelleSeanceFragment
import com.example.fsudouest.blablafit.Util.BottomNavigationViewBehavior
import com.example.fsudouest.blablafit.Util.BottomNavigationViewHelper
import com.example.fsudouest.blablafit.databinding.ActivityMainBinding
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>


    @Inject
    lateinit var mFirebaseAuth: FirebaseAuth

    private var mAuthStateListener: FirebaseAuth.AuthStateListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)


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

        NavigationUI.setupActionBarWithNavController(this,navController)
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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(this, R.id.myNavHostFragment)
        return navController.navigateUp()
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

}
