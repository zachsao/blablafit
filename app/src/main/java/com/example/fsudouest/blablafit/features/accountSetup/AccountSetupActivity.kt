package com.example.fsudouest.blablafit.features.accountSetup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fsudouest.blablafit.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountSetupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_setup_activity)
    }
}
