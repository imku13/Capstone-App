package com.imregulkurt.app_capstone.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imregulkurt.app_capstone.common.Resource
import com.imregulkurt.app_capstone.data.model.response.BaseResponse
import com.imregulkurt.app_capstone.data.model.response.ProductUI
import com.imregulkurt.app_capstone.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val productRepository: ProductRepository): ViewModel() {

    // We do not need firebase repository this time.

    private var _favoriteState = MutableLiveData<FavoriteState>()
    val favoriteState: LiveData<FavoriteState> get() = _favoriteState

    fun getFavoriteProducts() = viewModelScope.launch {
        _favoriteState.value = FavoriteState.Loading
        val response = productRepository.getFavProducts()
        _favoriteState.value = when (response) {
            is Resource.Success -> {
                FavoriteState.SuccessState(response.data)
            }
            is Resource.Fail -> {
                FavoriteState.EmptyScreen(response.failMessage)
            }
            is Resource.Error -> {
                FavoriteState.ShowPopUp(response.errorMessage)
            }
        }
    }

    fun removeFromFavorites(productUI: ProductUI) = viewModelScope.launch {
        _favoriteState.value = FavoriteState.Loading
        productRepository.removeFromFavorites(productUI)
        when (val response = productRepository.getFavProducts()) {
            is Resource.Success -> {
                FavoriteState.SuccessState(response.data)
                getFavoriteProducts()
            }
            is Resource.Fail -> {
                FavoriteState.EmptyScreen(response.failMessage)
            }
            is Resource.Error -> {
                FavoriteState.ShowPopUp(response.errorMessage)
            }
        }
    }
}

sealed interface FavoriteState {
    data object Loading : FavoriteState
    data class SuccessState(val products: List<ProductUI>) : FavoriteState
    data class EmptyScreen(val failMessage: String) : FavoriteState
    data class ShowPopUp(val errorMessage: String) : FavoriteState
}