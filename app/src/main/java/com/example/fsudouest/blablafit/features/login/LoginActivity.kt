package com.example.fsudouest.blablafit.features.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.ActivityLogin2Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLogin2Binding = DataBindingUtil.setContentView(this,R.layout.activity_login2)

        binding.startButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java).putExtra("destination", "register"))
        }
    }


}

