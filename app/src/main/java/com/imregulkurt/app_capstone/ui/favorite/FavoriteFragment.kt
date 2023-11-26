package com.imregulkurt.app_capstone.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.imregulkurt.app_capstone.R
import com.imregulkurt.app_capstone.common.gone
import com.imregulkurt.app_capstone.common.viewBinding
import com.imregulkurt.app_capstone.common.visible
import com.imregulkurt.app_capstone.data.model.response.ProductUI
import com.imregulkurt.app_capstone.databinding.FragmentFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private val binding by viewBinding(FragmentFavoriteBinding::bind)
    private val viewModel by viewModels<FavoriteViewModel>()

    private val favoriteProductAdapter = FavoriteAdapter(
        onProductClick = ::onProductClick,
        onFavoriteRemoveClick = ::onFavoriteRemoveClick
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavoriteProducts()

        with(binding) {
            recyclerView2.adapter = favoriteProductAdapter
            backButton3.setOnClickListener {
                findNavController().navigateUp()
            }
        }

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.favoriteState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavoriteState.Loading -> {
                    progressBar7.visible()
                    errorIconView4.gone()
                    errorText4.gone()
                }
                is FavoriteState.SuccessState -> {
                    progressBar7.gone()
                    errorIconView4.gone()
                    errorText4.gone()
                    favoriteProductAdapter.submitList(state.products)
                }
                is FavoriteState.EmptyScreen -> {
                    progressBar7.gone()
                    textView12.gone()
                    recyclerView2.gone()
                    errorIconView4.visible()
                    errorText4.visible()

                    val list = ArrayList<ProductUI>()
                    favoriteProductAdapter.submitList(list)
                }
                is FavoriteState.ShowPopUp -> {
                    progressBar7.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }

    private fun onProductClick(productId: Int) {
        findNavController().navigate(FavoriteFragmentDirections.actionFavoriteFragment2ToDetailFragment(productId))
    }

    private fun onFavoriteRemoveClick(productUI: ProductUI) {
        viewModel.removeFromFavorites(productUI)
        viewModel.getFavoriteProducts()
    }
}
