package com.imregulkurt.app_capstone.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imregulkurt.app_capstone.R
import com.imregulkurt.app_capstone.common.Resource
import com.imregulkurt.app_capstone.data.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository): ViewModel() {

    private var _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> get() = _loginState

    init {
        checkUserLoggedIn()
    }

    private fun checkUserLoggedIn() {
        if (firebaseRepository.isUserLoggedIn()) {
            _loginState.value = LoginState.GoToHome
        }
    }

    fun signIn(email: String, password: String) = viewModelScope.launch {
        checkUserLoggedIn()
        if (checkFields(email, password)) {
            _loginState.value = LoginState.Loading
            val response = firebaseRepository.signInWithEmailAndPassword(email, password)
            _loginState.value = when (response) {
                is Resource.Success -> LoginState.GoToHome
                is Resource.Fail -> LoginState.ShowPopUp(response.failMessage)
                is Resource.Error -> LoginState.ShowPopUp(response.errorMessage)
            }
        }
    }

    private fun checkFields(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                _loginState.value = LoginState.ShowPopUp(R.string.email_empty.toString())
                false
            }
            password.isEmpty() -> {
                _loginState.value = LoginState.ShowPopUp(R.string.password_empty.toString())
                false
            }
            password.length < 6 -> {
                _loginState.value = LoginState.ShowPopUp(R.string.password_short.toString())
                false
            }
            else -> true
        }
    }
}

sealed interface LoginState {
    data object Loading : LoginState
    data object GoToHome : LoginState
    data class ShowPopUp(val errorMessage: String) : LoginState
}