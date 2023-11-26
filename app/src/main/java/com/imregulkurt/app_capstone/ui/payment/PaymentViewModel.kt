package com.imregulkurt.app_capstone.ui.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imregulkurt.app_capstone.common.Resource
import com.imregulkurt.app_capstone.data.repository.FirebaseRepository
import com.imregulkurt.app_capstone.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(private val productRepository: ProductRepository, private val firebaseRepository: FirebaseRepository): ViewModel() {

    private val _paymentState = MutableLiveData<PaymentState>()
    val paymentState: LiveData<PaymentState> get() = _paymentState
}

sealed interface PaymentState {
    data object Loading : PaymentState
    data object SuccessState : PaymentState
    data class ShowPopUp(val errorMessage: String) : PaymentState
}
