package com.example.fsudouest.blablafit.features.login


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_basic_information.*
import javax.inject.Inject


class BasicInformationFragment : Fragment(), Injectable {

    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var viewModel: SignUpViewModel

    private lateinit var binding: com.example.fsudouest.blablafit.databinding.FragmentBasicInformationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_basic_information, container, false)

        viewModel = ViewModelProviders.of(this, factory).get(SignUpViewModel::class.java).apply {
            stateLiveData().observe(this@BasicInformationFragment, Observer {
                render(it)
            })
        }

        binding.nextStepButton.setOnClickListener { viewModel.submitForm() }

        return binding.root
    }



    private fun render(state: SignUpState?) {
        when(state){
            is SignUpState.ValidationError -> displayErrors(state.data.errors)
        }
    }

    private fun displayErrors(errors: List<SignUpError>) {
        errors.forEach {
            when (it) {
                is SignUpError.FirstNameEmpty -> firstNameInput.error = "Champs obligatoire"
                SignUpError.LastNameEmpty -> lastNameInput.error = "Champs obligatoire"
                SignUpError.EmailEmpty -> emailInput.error = "Champs obligatoire"
                SignUpError.PasswordEmpty -> passwordInput.error = "Champs obligatoire"
                SignUpError.ConfirmPasswordEmpty -> passwordConfirmInput.error = "Champs obligatoire"
                SignUpError.WrongPassword -> passwordConfirmInput.error = "Mots de passes différents"
                SignUpError.InvalidEmail -> emailInput.error = "Email invalide"
                SignUpError.InvalidPassword -> passwordInput.error = "Mot de passe invalide"
            }
        }
    }


}
