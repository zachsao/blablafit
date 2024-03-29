package com.example.fsudouest.blablafit.features.home


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.ActivityMainBinding
import com.example.fsudouest.blablafit.features.splash.SplashActivity
import com.example.fsudouest.blablafit.utils.BottomNavigationViewHelper
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var mFirebaseAuth: FirebaseAuth

    private var mAuthStateListener: FirebaseAuth.AuthStateListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        mAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                startActivity(Intent(this, SplashActivity::class.java))
                finish()
            }
        }

        val navController = findNavController(this, R.id.myNavHostFragment)

        val navigation = binding.bottomNavigation
        BottomNavigationViewHelper.removeShiftMode(navigation)
        setupWithNavController(navigation, navController)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.trouverUneSeanceFragment, R.id.seancesFragment,
                R.id.messagesFragment, R.id.myProfileFragment, R.id.typeSeanceFragment))

        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration)
    }

    override fun onResume() {
        super.onResume()
        mFirebaseAuth.addAuthStateListener(mAuthStateListener!!)
    }

    override fun onPause() {
        super.onPause()
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener!!)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(this, R.id.myNavHostFragment)
        return navController.navigateUp()
    }
}
