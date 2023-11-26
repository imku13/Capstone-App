package com.imregulkurt.app_capstone.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.imregulkurt.app_capstone.R
import com.imregulkurt.app_capstone.common.viewBinding
import com.imregulkurt.app_capstone.databinding.FragmentDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.imregulkurt.app_capstone.common.gone
import com.imregulkurt.app_capstone.common.visible
import com.imregulkurt.app_capstone.data.model.request.AddToCartRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val binding by viewBinding(FragmentDetailBinding::bind)
    private val viewModel by viewModels<DetailViewModel>()

    private val args by navArgs<DetailFragmentArgs>()
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid

        with(binding) {
            backButton1.setOnClickListener {
                findNavController().navigateUp()
            }
            addCartButton.setOnClickListener {
                val addToCartRequest = AddToCartRequest(userId.toString(), args.productId)
                viewModel.addToCart(addToCartRequest.productId ?: -1)
            }
        }

        viewModel.getProductDetail(args.productId)
        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.detailState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is DetailState.Loading -> {
                    progressBar4.visible()
                    errorIconView.gone()
                    errorText.gone()
                }
                is DetailState.SuccessState -> {
                    progressBar4.gone()
                    errorIconView.gone()
                    errorText.gone()

                    Glide.with(productImageView2).load(state.product.imageOne).into(productImageView2)
                    productTitle1.text = state.product.title
                    productPriceText2.text = state.product.price.toString()
                    productDescription.text = state.product.description

                    if (state.product.saleState) {
                        productPriceText2.setBackgroundResource(R.drawable.strike_through)
                        productOfferText3.text = state.product.salePrice.toString()
                    } else {
                        productOfferText3.visibility = View.GONE
                    }
                }
                is DetailState.EmptyScreen -> {
                    progressBar4.gone()
                    errorIconView.visible()
                    errorText.visible()
                    errorText.text = state.failMessage
                }
                is DetailState.ShowPopUp -> {
                    progressBar4.gone()
                    errorIconView.gone()
                    errorText.gone()

                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }
}
