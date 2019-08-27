package com.example.fsudouest.blablafit.features.login.signIn

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.R
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

private const val TAG = "SignInViewModel"

class SignInViewModel @Inject constructor(private val firebaseAuth: FirebaseAuth): ViewModel() {

    private val stateLiveData = MutableLiveData<SignInState>()

    fun stateLiveData(): LiveData<SignInState> = stateLiveData

    init {
        stateLiveData.value = SignInState.Idle(SignInData())
    }

    fun submitForm(){
        stateLiveData.value?.data?.let {
            if (checkForm(it)){
                firebaseAuth.signInWithEmailAndPassword(it.email, it.password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success")
                                stateLiveData.value = SignInState.Success(it.copy(errors = listOf()))
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.e(TAG, "createUserWithEmail:failure", task.exception)
                                stateLiveData.value = SignInState.Failure(it.copy(errors = listOf()), task.exception?.message ?: "")
                            }
                        }
            }
        }
    }

    private fun checkForm(data: SignInData): Boolean {
        val errors = mutableListOf<SignInError>()
        if (data.email.isEmpty()) errors.add(SignInError.EmailEmpty)
        else if (!Patterns.EMAIL_ADDRESS.matcher(data.email).matches()) {
            errors.add(SignInError.InvalidEmail(R.string.invalid_email))
        }
        if (data.password.isEmpty()) errors.add(SignInError.PasswordEmpty)
        else if (data.password.length < 8) errors.add(SignInError.InvalidPassword)

        if (errors.isNotEmpty()){
            stateLiveData.value = SignInState.ValidationError(data.copy(errors = errors))
        }
        return (errors.isEmpty())
    }

    fun emailChanged(newText: String) {
        val data = stateLiveData.value?.data
        data?.let { stateLiveData.value = SignInState.TextChanged(data.copy(email = newText)) }
    }

    fun passwordChanged(newText: String) {
        val data = stateLiveData.value?.data
        data?.let { stateLiveData.value = SignInState.TextChanged(data.copy(password = newText)) }
    }
}