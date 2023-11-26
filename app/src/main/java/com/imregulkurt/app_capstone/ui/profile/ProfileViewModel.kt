package com.imregulkurt.app_capstone.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imregulkurt.app_capstone.R
import com.imregulkurt.app_capstone.common.Resource
import com.imregulkurt.app_capstone.data.repository.FirebaseRepository
import com.imregulkurt.app_capstone.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val productRepository: ProductRepository, private val firebaseRepository: FirebaseRepository): ViewModel() {

    // Did not need productRepository but did not remove from the constructor

    fun getUserEmail() = firebaseRepository.getUserEmail()

    fun logOut() = viewModelScope.launch {
        firebaseRepository.logOut()
    }
}
