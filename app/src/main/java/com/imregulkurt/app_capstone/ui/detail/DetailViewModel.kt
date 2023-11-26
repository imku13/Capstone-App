package com.imregulkurt.app_capstone.ui.detail

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
class DetailViewModel @Inject constructor(private val productRepository: ProductRepository, private val firebaseRepository: FirebaseRepository): ViewModel() {

    private var _detailState = MutableLiveData<DetailState>()
    val detailState: LiveData<DetailState> get() = _detailState

    private var detailData: ProductUI? = null

    fun getProductDetail(id: Int) = viewModelScope.launch {
        _detailState.value = DetailState.Loading
        _detailState.value = when (val result = productRepository.getProductDetail(id)) {
            is Resource.Success -> {
                detailData = result.data
                DetailState.SuccessState(result.data)
            }
            is Resource.Fail -> {
                DetailState.EmptyScreen(result.failMessage)
            }
            is Resource.Error -> {
                DetailState.ShowPopUp(result.errorMessage)
            }
        }
    }

    fun addToCart(productId: Int) = viewModelScope.launch {

        _detailState.value = DetailState.Loading

        when(val result = productRepository.addToCart(firebaseRepository.getUserId(), productId)) {
            is Resource.Success -> {
                _detailState.value = DetailState.ShowPopUp(result.data.message.toString())
            }
            is Resource.Error -> {
                _detailState.value = DetailState.ShowPopUp(result.errorMessage)
            }
            is Resource.Fail -> {
                _detailState.value = DetailState.EmptyScreen(result.failMessage)
            }
        }
    }
}

sealed interface DetailState {
    data object Loading : DetailState
    data class SuccessState(val product: ProductUI) : DetailState
    data class ShowPopUp(val errorMessage: String) : DetailState
    data class EmptyScreen(val failMessage: String) : DetailState
}
