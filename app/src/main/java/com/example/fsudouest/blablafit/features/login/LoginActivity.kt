package com.example.fsudouest.blablafit.features.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.fsudouest.blablafit.MainActivity
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.ActivityLogin2Binding

import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse

import java.util.Arrays

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLogin2Binding = DataBindingUtil.setContentView(this,R.layout.activity_login2)

        binding.startButton.setOnClickListener { signIn() }
    }

    private fun signIn() {
        // Choose authentication providers
        val providers = Arrays.asList<AuthUI.IdpConfig>(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build())
        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.logo)
                        .build(),
                RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                if (response != null) {
                    val builder = AlertDialog.Builder(this)

                    builder.setTitle("Error " + response.error!!.errorCode)
                    builder.setMessage(response.error!!.toString())
                    // Add the button
                    builder.setPositiveButton("OK") { dialog, id ->
                        // User clicked OK button
                    }
                    // Create the AlertDialog
                    val dialog = builder.create()
                    dialog.show()
                }

            }
        }
    }


}

