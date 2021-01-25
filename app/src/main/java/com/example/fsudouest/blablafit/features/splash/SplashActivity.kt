package com.example.fsudouest.blablafit.features.splash

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.features.accountSetup.AccountSetupActivity
import com.example.fsudouest.blablafit.features.home.MainActivity
import com.example.fsudouest.blablafit.features.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.startActivity
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var mFirebaseAuth: FirebaseAuth

    private val viewModel: SplashViewModel by viewModels()

    private var mAuthStateListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                startActivity<LoginActivity>()
                finish()
            }else {
                val uid = user.uid
                viewModel.userIsSetup(uid)
                viewModel.stateLiveData().observe(this, { state ->
                    when (state) {
                        is SplashState.UserLoaded -> redirect(state.isSetup)
                    }
                })
            }
        }
    }

    private fun redirect(setup: Boolean?) {
        when (setup) {
            null -> startActivity<LoginActivity>()
            true -> startActivity<MainActivity>()
            false -> startActivity<AccountSetupActivity>()
        }
        finish()
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
