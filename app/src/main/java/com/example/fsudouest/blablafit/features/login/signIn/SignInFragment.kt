package com.example.fsudouest.blablafit.features.login.signIn


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.features.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_signin_form.*

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private val viewModel: SignInViewModel by viewModels()

    private lateinit var binding: com.example.fsudouest.blablafit.databinding.FragmentSigninFormBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signin_form, container, false)

        viewModel.stateLiveData().observe(viewLifecycleOwner, {
            render(it)
        })

        addEmailTextListener()
        addPasswordTextListener()

        binding.loginButton.setOnClickListener { viewModel.submitForm() }
        binding.registerTextview.setOnClickListener {
            findNavController(this).navigate(R.id.action_basicInformationFragment_to_registerFragment2)
        }

        return binding.root
    }

    private fun addPasswordTextListener() {
        binding.passwordEdit.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                viewModel.passwordChanged(p0.toString())
            }
        })
    }

    private fun addEmailTextListener() {
        binding.emailEdit.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                viewModel.emailChanged(p0.toString())
            }
        })
    }


    private fun render(state: SignInState?) {
        when (state) {
            is SignInState.ValidationError -> displayErrors(state.data.errors)
            is SignInState.Failure -> displayDialog(state.message)
            is SignInState.Success -> {
                viewModel.setRegistrationToken()
                goToMainActivity()
            }
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        activity?.finish()
    }

    private fun displayDialog(message: String) {
        val title = "Failure"
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(title)
                setPositiveButton(R.string.ok
                ) { _, _ -> }
                setMessage(message)
            }
            // Create the AlertDialog
            builder.create().show()
        }
    }

    private fun displayErrors(errors: List<SignInError>) {
        errors.forEach {
            when (it) {
                SignInError.EmailEmpty -> emailInput.error = getString(R.string.mandatory_field)
                SignInError.PasswordEmpty -> passwordInput.error = getString(R.string.mandatory_field)
                is SignInError.InvalidEmail -> emailInput.error = getString(it.stringId)
                SignInError.InvalidPassword -> passwordInput.error = getString(R.string.invalid_password)
            }
        }
    }


}
