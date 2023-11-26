package com.imregulkurt.app_capstone.ui.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.imregulkurt.app_capstone.R
import com.imregulkurt.app_capstone.common.gone
import com.imregulkurt.app_capstone.common.viewBinding
import com.imregulkurt.app_capstone.common.visible
import com.imregulkurt.app_capstone.data.model.response.ProductUI
import com.imregulkurt.app_capstone.databinding.FragmentCartBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment: Fragment(R.layout.fragment_cart) {

    private val binding by viewBinding(FragmentCartBinding::bind)
    private val viewModel by viewModels<CartViewModel>()

    private val cartProductsAdapter = CartAdapter(
        onProductClick = ::onProductClick,
        onCartRemoveClick = ::onCartRemoveClick
    )

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid

        viewModel.getCartProducts(userId ?: "")
        viewModel.totalPrice.observe(viewLifecycleOwner) { price ->
            binding.cartPriceText.text = price.toString()
        }

        with(binding) {
            recyclerView.adapter = cartProductsAdapter
            backButton8.setOnClickListener {
                findNavController().navigateUp()
            }
            purchaseButton.setOnClickListener {
                if (cartProductsAdapter.currentList.isNotEmpty()) {
                    findNavController().navigate(CartFragmentDirections.actionCartFragment2ToPaymentFragment())
                } else {
                    Snackbar.make(requireView(), "Shopping cart is empty!", Snackbar.LENGTH_LONG).show()
                }
            }
        }

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.cartState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CartState.Loading -> {
                    progressBar5.visible()
                    errorIconView3.gone()
                    errorText3.gone()
                }
                is CartState.SuccessState -> {
                    progressBar5.gone()
                    errorIconView3.gone()
                    errorText3.gone()
                    cartProductsAdapter.submitList(state.products)
                }
                is CartState.EmptyScreen -> {
                    progressBar5.gone()
                    cartTitleText.gone()
                    totalPriceText.gone()
                    cartPriceText.gone()
                    purchaseButton.gone()
                    constraintLayout7.gone()
                    errorIconView3.visible()
                    errorText3.visible()

                    val list = ArrayList<ProductUI>()
                    cartProductsAdapter.submitList(list)
                    viewModel.setTotalPrice(0.0)
                }
                is CartState.DeleteFromCart -> {
                    Snackbar.make(requireView(), state.baseResponse.message.toString(), 1000).show()
                }
                is CartState.ShowPopUp -> {
                    progressBar5.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }

    private fun onProductClick(productId: Int) {
        findNavController().navigate(CartFragmentDirections.actionCartFragment2ToDetailFragment4(productId))
    }

    private fun onCartRemoveClick(id: Int, userId: String) {
        viewModel.deleteFromCart(id, userId)
        viewModel.getCartProducts(userId)
    }
}
