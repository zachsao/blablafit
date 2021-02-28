package com.example.fsudouest.blablafit.features.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.features.accountSetup.AccountSetupActivity
import com.example.fsudouest.blablafit.features.home.MainActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.startActivity
import javax.inject.Inject

private const val RC_SIGN_IN = 123

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var mFirebaseAuth: FirebaseAuth

    private val viewModel: SplashViewModel by viewModels()

    private var mAuthStateListener: FirebaseAuth.AuthStateListener? = null

    private val providers = listOf(AuthUI.IdpConfig.GoogleBuilder().build())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                signIn()
            }else {
                val uid = user.uid
                viewModel.userIsSetup(uid)
            }
        }

        viewModel.stateLiveData().observe(this, { state ->
            when (state) {
                is SplashState.UserLoaded -> redirect(state.isSetup)
            }
        })
    }

    private fun signIn() {
        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setAlwaysShowSignInMethodScreen(true)
                        .setTheme(R.style.LoginTheme)
                        .build(),
                RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            response?.let {
                if (resultCode == Activity.RESULT_OK) {
                    if (response.isNewUser) {
                        val user = mFirebaseAuth.currentUser
                        user?.email?.let { email -> user.displayName?.let { name -> viewModel.saveUserToFirestore(user.uid, email, name, user.photoUrl?.toString()) } }
                    }
                } else {
                    showErrorDialog(it)
                }
            }
        }
    }

    private fun showErrorDialog(response: IdpResponse) {
        val builder = AlertDialog.Builder(this).apply {
            setTitle("Sign in error " + response.error?.errorCode)
            setMessage(response.error!!.toString())
            setPositiveButton("OK") { _, _ -> }
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun redirect(setup: Boolean?) {
        when (setup) {
            null -> signIn()
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
