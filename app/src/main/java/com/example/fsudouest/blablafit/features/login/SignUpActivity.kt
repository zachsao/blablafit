package com.example.fsudouest.blablafit.features.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.fsudouest.blablafit.R
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class SignUpActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        intent.getStringExtra("destination")?.let {
            findNavController(R.id.signUpNavHostFragment).navigate(R.id.action_basicInformationFragment_to_registerFragment2)
        }
    }

    override fun androidInjector() = androidInjector
}
