package com.example.fsudouest.blablafit.features.accountSetup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class AccountSetupViewModel @Inject constructor(): ViewModel() {
    private val genderLiveData = MutableLiveData<Boolean>()

    init {
        genderLiveData.value = true
    }

    fun genderLiveData() = genderLiveData

    fun updateGender(select: Boolean){
        if (genderLiveData.value != select) genderLiveData.value = select
    }
}
