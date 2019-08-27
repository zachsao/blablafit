package com.example.fsudouest.blablafit.features.login.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.R
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class RegisterViewModel @Inject constructor(mDatabase: FirebaseFirestore): ViewModel() {

    private val stateLiveData = MutableLiveData<RegisterState>()

    init {
        stateLiveData.value = RegisterState.Idle(RegisterData())
    }

    fun stateLiveData(): LiveData<RegisterState> = stateLiveData

    fun submitForm(){
        if (checkForm()) signUp()
    }

    private fun signUp() {
        //TODO()
    }

    private fun checkForm(): Boolean{
        val errors = mutableListOf<RegisterError>()
        stateLiveData.value?.let { state ->
            val data = state.data

            if (data.fullName.isEmpty()) errors.add(RegisterError.FullNameEmpty(R.string.mandatory_field))
            else if (!data.fullName.contains(" ")) errors.add(RegisterError.FullNameIncomplete(R.string.incomplete_fullname))

            if (data.email.isEmpty()) errors.add(RegisterError.EmailEmpty(R.string.mandatory_field))
            else if (!Patterns.EMAIL_ADDRESS.matcher(data.email).matches()) {
                errors.add(RegisterError.InvalidEmail(R.string.invalid_email))
            }
            if (data.password.isEmpty()) errors.add(RegisterError.PasswordEmpty(R.string.mandatory_field))
            else if (data.password.length < 8) errors.add(RegisterError.InvalidPassword(R.string.error_invalid_password))

            if (data.confirmPassword.isEmpty()) errors.add(RegisterError.ConfirmPasswordEmpty(R.string.mandatory_field))
            else if (data.confirmPassword != data.password) errors.add(RegisterError.WrongPassword(R.string.wrong_password))

            if (errors.isNotEmpty()){
                stateLiveData.value = RegisterState.ValidationError(data.copy(errors = errors))
            }
        }
        return errors.isEmpty()
    }

    fun updateState(id: Int, text: String) {
        val data = stateLiveData.value?.data ?: RegisterData()
        when(id){
            R.id.nameEdit -> stateLiveData.value = RegisterState.Idle(data.copy(fullName = text))
            R.id.emailEdit -> stateLiveData.value = RegisterState.Idle(data.copy(email = text))
            R.id.passwordEdit -> stateLiveData.value = RegisterState.Idle(data.copy(password = text))
            R.id.confirmPasswordEdit -> stateLiveData.value = RegisterState.Idle(data.copy(confirmPassword = text))
        }
    }
}