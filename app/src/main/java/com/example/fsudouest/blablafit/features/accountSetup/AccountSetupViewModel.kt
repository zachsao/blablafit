package com.example.fsudouest.blablafit.features.accountSetup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.accountSetup.basicinformation.BasicInformationData
import com.example.fsudouest.blablafit.features.accountSetup.basicinformation.BasicInformationState
import com.example.fsudouest.blablafit.features.accountSetup.basicinformation.ValidationError
import javax.inject.Inject

class AccountSetupViewModel @Inject constructor(): ViewModel() {
    private val infoStateLiveData = MutableLiveData<BasicInformationState>()
    private val genderLiveData = MutableLiveData<Boolean>()

    init {
        infoStateLiveData.value = BasicInformationState.Idle(BasicInformationData())
        genderLiveData.value = true
    }

    fun infoStateLiveData(): LiveData<BasicInformationState> = infoStateLiveData
    fun genderLiveData(): LiveData<Boolean> = genderLiveData

    fun dateChanged(date: String) {
        infoStateLiveData.value = BasicInformationState.DateUpdated(previousStateData()
                .copy(
                        birthday = date,
                        errors = previousStateData().errors.filter { it !is ValidationError.BirthDateEmpty }
                )
        )
    }
    private fun previousStateData() = infoStateLiveData.value?.data ?: BasicInformationData()
    fun submitBasicInfoForm() {
        infoStateLiveData.value = checkForm(previousStateData())
    }

    private fun checkForm(data: BasicInformationData): BasicInformationState {
        val errors = mutableListOf<ValidationError>()
        if (data.birthday.isEmpty()) errors.add(ValidationError.BirthDateEmpty)
        if (data.city.isEmpty()) errors.add(ValidationError.CityEmpty)

        return if (errors.isEmpty()) BasicInformationState.Success(data)
            else BasicInformationState.Error(data.copy(errors = errors))
    }

    fun updateCity(placeName: String?) {
        infoStateLiveData.value = BasicInformationState.CityUpdated(previousStateData()
                .copy(
                        city = placeName ?: "",
                        errors = previousStateData().errors.filter { it !is ValidationError.CityEmpty }
                )
        )
    }

    fun updateGender(isMale: Boolean) {
        val previousGender = genderLiveData.value
        if (previousGender != isMale) genderLiveData.value = isMale
    }
}
