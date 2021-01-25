package com.example.fsudouest.blablafit.features.login.register

import android.util.Patterns
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber


class RegisterViewModel @ViewModelInject constructor(private val mDatabase: FirebaseFirestore, private val auth: FirebaseAuth) : ViewModel() {

    private val stateLiveData = MutableLiveData<RegisterState>()
    private val registerSuccessLiveData = MutableLiveData<Boolean>()

    init {
        stateLiveData.value = RegisterState.Idle(RegisterData())
    }

    fun stateLiveData(): LiveData<RegisterState> = stateLiveData
    fun registerStatusLiveData(): LiveData<Boolean> = registerSuccessLiveData

    fun submitForm(){
        if (checkForm()) signUpUser()
    }

    private fun signUpUser() {
        val data = stateLiveData.value?.data ?: RegisterData()
        val user = data.let { User(it.fullName, it.email) }
        auth.createUserWithEmailAndPassword(data.email, data.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val currentUser = auth.currentUser
                        Timber.d("RegisterViewModel: successfully created user with id: ${currentUser?.uid}")
                        if (currentUser != null) saveUserToFirestore(currentUser.uid, user.copy(uid = currentUser.uid))
                    } else {
                        Timber.e(task.exception)
                        return@addOnCompleteListener
                    }
                }
    }

    private fun saveUserToFirestore(userId: String, user: User) {
        mDatabase.collection("users")
                .document(userId)
                .set(user)
                .addOnSuccessListener {
                    registerSuccessLiveData.value = true
                    Timber.d("saved document successfully")
                }
                .addOnFailureListener {
                    Timber.d("RegisterViewModel: failed to save document : $it")
                }
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