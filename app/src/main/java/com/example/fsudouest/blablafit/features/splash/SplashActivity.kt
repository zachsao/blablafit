package com.example.fsudouest.blablafit.features.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.features.accountSetup.AccountSetupActivity
import com.example.fsudouest.blablafit.features.home.MainActivity
import com.example.fsudouest.blablafit.features.login.LoginActivity
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import dagger.android.AndroidInjection
import org.jetbrains.anko.startActivity
import javax.inject.Inject

class SplashActivity : AppCompatActivity(), Injectable {

    @Inject
    lateinit var mFirebaseAuth: FirebaseAuth
    @Inject
    lateinit var factory: ViewModelFactory<SplashViewModel>

    private val viewModel by lazy { ViewModelProviders.of(this, factory)[SplashViewModel::class.java] }

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
                viewModel.stateLiveData().observe(this, Observer { state ->
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
