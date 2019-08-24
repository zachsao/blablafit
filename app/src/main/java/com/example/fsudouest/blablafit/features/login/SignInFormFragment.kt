package com.example.fsudouest.blablafit.features.login


import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.fsudouest.blablafit.MainActivity

import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_signin_form.*
import javax.inject.Inject


class SignInFormFragment : Fragment(), Injectable {

    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var viewModel: SignInViewModel

    private lateinit var binding: com.example.fsudouest.blablafit.databinding.FragmentSigninFormBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signin_form, container, false)

        viewModel = ViewModelProviders.of(this, factory).get(SignInViewModel::class.java).apply {
            stateLiveData().observe(this@SignInFormFragment, Observer {
                render(it)
            })
        }

        addEmailTextListener()
        addPasswordTextListener()

        binding.loginButton.setOnClickListener { viewModel.submitForm() }

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
            is SignInState.Success -> goToMainActivity()
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

    private fun displayErrors(errors: List<SignUpError>) {
        errors.forEach {
            when (it) {
                SignUpError.EmailEmpty -> emailInput.error = getString(R.string.mandatory_field)
                SignUpError.PasswordEmpty -> passwordInput.error = getString(R.string.mandatory_field)
                is SignUpError.InvalidEmail -> emailInput.error = getString(it.stringId)
                SignUpError.InvalidPassword -> passwordInput.error = getString(R.string.invalid_password)
            }
        }
    }


}
