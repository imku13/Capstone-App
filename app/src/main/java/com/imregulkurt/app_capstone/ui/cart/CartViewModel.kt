package com.imregulkurt.app_capstone.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imregulkurt.app_capstone.common.Resource
import com.imregulkurt.app_capstone.data.model.request.DeleteFromCartRequest
import com.imregulkurt.app_capstone.data.model.response.BaseResponse
import com.imregulkurt.app_capstone.data.model.response.ProductUI
import com.imregulkurt.app_capstone.data.repository.FirebaseRepository
import com.imregulkurt.app_capstone.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val productRepository: ProductRepository, private val authRepository: FirebaseRepository): ViewModel() {

    private var _cartState = MutableLiveData<CartState>()
    val cartState: LiveData<CartState> get() = _cartState

    private val _totalPrice = MutableLiveData(0.0)
    val totalPrice: LiveData<Double> = _totalPrice

    init {
        _totalPrice.value = 0.0
    }

    fun setTotalPrice(price: Double) {
        _totalPrice.value = price
    }

    fun getCartProducts(userId: String) = viewModelScope.launch {
        _cartState.value = CartState.Loading
        val response = productRepository.getCartProducts(userId)
        var tempValue = 0.0
        _cartState.value = when (response) {
            is Resource.Success -> {
                if (response.data.isNullOrEmpty()) {
                    _totalPrice.value = 0.0
                } else {
                    for (product in response.data){
                        if (product.saleState){
                            tempValue += product.salePrice
                        } else {
                            tempValue += product.price
                        }
                    }
                    _totalPrice.value = tempValue
                }

                CartState.SuccessState(response.data)
            }
            is Resource.Fail -> {
                CartState.EmptyScreen(response.failMessage)
            }
            is Resource.Error -> {
                CartState.ShowPopUp(response.errorMessage)
            }
        }
    }

    fun deleteFromCart(id: Int, userId: String) = viewModelScope.launch {
        _cartState.value = CartState.Loading
        val deleteFromCartRequest = DeleteFromCartRequest(userId, id)
        when (val response = productRepository.removeFromCart(deleteFromCartRequest.userId.toString(), deleteFromCartRequest.id ?: -1)) {
            is Resource.Success -> {
                CartState.DeleteFromCart(response.data)
                getCartProducts(authRepository.getUserId())
            }
            is Resource.Fail -> {
                CartState.EmptyScreen(response.failMessage)
            }
            is Resource.Error -> {
                CartState.ShowPopUp(response.errorMessage)
            }
        }
    }
}

sealed interface CartState {
    data object Loading : CartState
    data class SuccessState(val products: List<ProductUI>) : CartState
    data class DeleteFromCart(val baseResponse: BaseResponse) : CartState
    data class EmptyScreen(val failMessage: String) : CartState
    data class ShowPopUp(val errorMessage: String) : CartState
}