package com.example.fsudouest.blablafit.features.filters

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import javax.inject.Inject

class FiltersActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelFactory<FiltersViewModel>

    private val viewModel by lazy { ViewModelProvider(this, factory).get(FiltersViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filters)
    }
}
