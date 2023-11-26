package com.imregulkurt.app_capstone.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.imregulkurt.app_capstone.R
import com.imregulkurt.app_capstone.common.gone
import com.imregulkurt.app_capstone.common.viewBinding
import com.imregulkurt.app_capstone.common.visible
import com.imregulkurt.app_capstone.data.model.response.ProductUI
import com.imregulkurt.app_capstone.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.imregulkurt.app_capstone.ui.splash.SplashFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel by viewModels<HomeViewModel>()

    private val productAdapter = ProductsAdapter(onProductClick = ::onProductClick, onFavoriteClick = ::onFavoriteClick)
    private val offerProductAdapter = OfferProductAdapter(onProductClick = ::onProductClick, onFavoriteClick = ::onFavoriteClick)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllProducts()
        viewModel.getSaleProducts()

        with(binding) {
            linearRecycler.adapter = productAdapter
            horizontalRecycler.adapter = offerProductAdapter
        }

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.homeState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is HomeState.Loading -> {
                    progressBar.visible()
                    errorIconView.gone()
                    errorText.gone()
                }
                is HomeState.SuccessState -> {
                    progressBar.gone()
                    errorIconView.gone()
                    errorText.gone()

                    productAdapter.submitList(state.products)
                }
                is HomeState.EmptyScreen -> {
                    progressBar.gone()
                    currentOffersText.gone()
                    allProductsText.gone()
                    errorIconView.visible()
                    errorText.visible()
                    errorText.text = state.failMessage
                }
                is HomeState.ShowPopUp -> {
                    progressBar.gone()
                    errorIconView.gone()
                    errorText.gone()

                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }

        viewModel.saleState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is HomeState.Loading -> {
                    progressBar.visible()
                    errorIconView.gone()
                    errorText.gone()
                }
                is HomeState.SuccessState -> {
                    progressBar.gone()
                    errorIconView.gone()
                    errorText.gone()

                    offerProductAdapter.submitList(state.products)
                }
                is HomeState.EmptyScreen -> {
                    progressBar.gone()
                    errorIconView.visible()
                    errorText.visible()
                    errorText.text = state.failMessage
                }
                is HomeState.ShowPopUp -> {
                    progressBar.gone()
                    errorIconView.gone()
                    errorText.gone()

                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }

    private fun onProductClick(productId: Int) {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragment2ToDetailFragment4(productId))
    }

    private fun onFavoriteClick(product: ProductUI) {
        viewModel.setFavoriteState(product)
    }
}
