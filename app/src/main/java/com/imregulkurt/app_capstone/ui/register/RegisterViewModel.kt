package com.imregulkurt.app_capstone.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imregulkurt.app_capstone.common.Resource
import com.imregulkurt.app_capstone.data.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository): ViewModel() {

    private var _registerState = MutableLiveData<RegisterState>()
    val registerState: LiveData<RegisterState> get() = _registerState

    fun registerUser(email: String, password: String) = viewModelScope.launch {
        if (checkFields(email, password)) {
            _registerState.value = RegisterState.Loading
            val response = firebaseRepository.signUpWithEmailAndPassword(email, password)
            _registerState.value = when (response) {
                is Resource.Success -> RegisterState.GoToHome
                is Resource.Fail -> RegisterState.ShowPopUp(response.failMessage)
                is Resource.Error -> RegisterState.ShowPopUp(response.errorMessage)
            }
        }
    }

    private fun checkFields(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                _registerState.value = RegisterState.ShowPopUp("Please fill email")
                false
            }
            password.isEmpty() -> {
                _registerState.value = RegisterState.ShowPopUp("Please fill password")
                false
            }
            password.length < 6 -> {
                _registerState.value =
                    RegisterState.ShowPopUp("Password can not be shorter than 6 characters")
                false
            }
            else -> true
        }
    }
}

sealed interface RegisterState {
    data object Loading : RegisterState
    data object GoToHome : RegisterState
    data class ShowPopUp(val errorMessage: String) : RegisterState
}