package com.example.fsudouest.blablafit.features.accountSetup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fsudouest.blablafit.R
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class AccountSetupActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_setup_activity)
    }

    override fun androidInjector() = androidInjector

}
