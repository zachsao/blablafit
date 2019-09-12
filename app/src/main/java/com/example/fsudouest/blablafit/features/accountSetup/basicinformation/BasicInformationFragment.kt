package com.example.fsudouest.blablafit.features.accountSetup.basicinformation

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.features.accountSetup.AccountSetupViewModel
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import kotlinx.android.synthetic.main.basic_information_fragment.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.textColor
import javax.inject.Inject

class BasicInformationFragment : Fragment(), Injectable {
    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var viewModel: AccountSetupViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.basic_information_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory).get(AccountSetupViewModel::class.java)
        viewModel.genderLiveData().observe(this, Observer {
            when (it){
                false -> {
                    select(female)
                    unselect(male)
                }
                true -> {
                    select(male)
                    unselect(female)
                }
            }
        })
        male.setOnClickListener {
            viewModel.updateGender(true)
        }
        female.setOnClickListener { viewModel.updateGender(false) }

    }

    private fun select(gender: Button) {
        gender.background = resources.getDrawable(R.drawable.round_corner)
        gender.textColor = resources.getColor(R.color.white)
    }

    private fun unselect(gender: Button) {
        gender.background = resources.getDrawable(R.drawable.round_corner_white)
        gender.textColor = resources.getColor(R.color.black)
    }

}
