package com.example.fsudouest.blablafit.features.login.register


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.FragmentRegisterBinding
import com.example.fsudouest.blablafit.features.accountSetup.AccountSetupActivity
import com.example.fsudouest.blablafit.service.MyFirebaseMessagingService
import com.example.fsudouest.blablafit.utils.FirestoreUtil
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.newTask
import org.jetbrains.anko.support.v4.intentFor

@AndroidEntryPoint
class RegisterFragment : Fragment() {


    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)

        viewModel.stateLiveData().observe(viewLifecycleOwner, { state ->
            render(state)
        })
        viewModel.registerStatusLiveData().observe(viewLifecycleOwner, { isRegistered ->
            if (isRegistered) {
                FirestoreUtil.getRegistrationToken { newRegistrationToken ->
                    MyFirebaseMessagingService.addTokenToFirestore(newRegistrationToken)
                }
                startActivity(intentFor<AccountSetupActivity>(
                        "userName" to binding.nameEdit.text.toString()
                ).newTask().clearTask())
            }
        })

        binding.registerButton.setOnClickListener { viewModel.submitForm() }
        binding.nameEdit.setTextChangedListener()
        binding.emailEdit.setTextChangedListener()
        binding.passwordEdit.setTextChangedListener()
        binding.confirmPasswordEdit.setTextChangedListener()

        return binding.root
    }

    private fun render(state: RegisterState) {
        when (state) {
            is RegisterState.ValidationError -> updateForm(state.data.errors)
        }
    }

    private fun updateForm(errors: List<RegisterError>) {
        errors.forEach { error ->
            when(error){
                is RegisterError.FullNameEmpty -> nameInput.error = getString(error.stringId)
                is RegisterError.EmailEmpty -> emailInput.error = getString(error.stringId)
                is RegisterError.PasswordEmpty -> passwordInput.error = getString(error.stringId)
                is RegisterError.ConfirmPasswordEmpty -> confirmPasswordInput.error = getString(error.stringId)
                is RegisterError.InvalidEmail -> emailInput.error = getString(error.stringId)
                is RegisterError.InvalidPassword -> passwordInput.error = getString(error.stringId)
                is RegisterError.WrongPassword -> confirmPasswordInput.error = getString(error.stringId)
                is RegisterError.FullNameIncomplete -> nameInput.error = getString(error.stringId)
            }
        }
    }

    private fun TextInputEditText.setTextChangedListener(){
        this.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.updateState(id, p0.toString())
            }

        })
    }
}
