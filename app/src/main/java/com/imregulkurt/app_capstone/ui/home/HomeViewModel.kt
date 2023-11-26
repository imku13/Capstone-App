package com.imregulkurt.app_capstone.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imregulkurt.app_capstone.common.Resource
import com.imregulkurt.app_capstone.data.model.response.ProductUI
import com.imregulkurt.app_capstone.data.repository.FirebaseRepository
import com.imregulkurt.app_capstone.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val productRepository: ProductRepository, private val firebaseRepository: FirebaseRepository): ViewModel() {

    private var _homeState = MutableLiveData<HomeState>()
    val homeState: LiveData<HomeState> get() = _homeState

    private val _saleState = MutableLiveData<HomeState>()
    val saleState: LiveData<HomeState> get() = _saleState


    fun getAllProducts() = viewModelScope.launch {
        _homeState.value = HomeState.Loading
        val response = productRepository.getAllProducts()
        _homeState.value = when (response) {
            is Resource.Success -> HomeState.SuccessState(response.data)
            is Resource.Fail -> HomeState.EmptyScreen(response.failMessage)
            is Resource.Error -> HomeState.ShowPopUp(response.errorMessage)
        }
    }

    fun getSaleProducts() = viewModelScope.launch {
        _saleState.value = HomeState.Loading
        val response = productRepository.getSaleProducts()
        _saleState.value = when (response) {
            is Resource.Success -> HomeState.SuccessState(response.data)
            is Resource.Fail -> HomeState.EmptyScreen(response.failMessage)
            is Resource.Error -> HomeState.ShowPopUp(response.errorMessage)
        }
    }

    // Moved to profile view model
    fun logOut() = viewModelScope.launch {
        firebaseRepository.logOut()
    }

    fun setFavoriteState(product: ProductUI) = viewModelScope.launch {
        if (product.favState) {
            productRepository.removeFromFavorites(product)
        } else {
            productRepository.addToFavorites(product)
        }
        getAllProducts()
        getSaleProducts()
    }
}

sealed interface HomeState {
    data object Loading : HomeState
    data class SuccessState(val products: List<ProductUI>) : HomeState
    data class EmptyScreen(val failMessage: String) : HomeState
    data class ShowPopUp(val errorMessage: String) : HomeState
}