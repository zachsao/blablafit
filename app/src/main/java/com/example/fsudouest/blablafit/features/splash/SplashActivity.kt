package com.example.fsudouest.blablafit.features.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fsudouest.blablafit.features.home.MainActivity
import com.example.fsudouest.blablafit.features.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.android.AndroidInjection
import org.jetbrains.anko.startActivity
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var mFirebaseAuth: FirebaseAuth

    private var mAuthStateListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("Default", Context.MODE_PRIVATE)

        mAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }else {
                val uid = user.uid
                val hasAccountSetup = sharedPreferences.getBoolean("IsSetup:$uid",false)
                // if (hasAccountSetup)
                startActivity<MainActivity>()
                //else startActivity<AccountSetupActivity>()
                finish()
            }
        }
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
}
