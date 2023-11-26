package com.imregulkurt.app_capstone.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.imregulkurt.app_capstone.R
import com.imregulkurt.app_capstone.data.model.response.ProductUI
import com.imregulkurt.app_capstone.databinding.ItemCartBinding

class CartAdapter(private val onProductClick: (Int) -> Unit, private val onCartRemoveClick: (Int, String) -> Unit): ListAdapter<ProductUI, CartAdapter.CartProductViewHolder>(ProductDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        return CartProductViewHolder(
            ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onProductClick,
            onCartRemoveClick
        )
    }

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) =
        holder.bind(getItem(position))

    class CartProductViewHolder(
        private val binding: ItemCartBinding,
        private val onProductClick: (Int) -> Unit,
        private val onCartRemoveClick: (Int, String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) {
            with(binding) {
                productText.text = product.title
                productPriceText.text = product.price.toString()
                Glide.with(productImageView).load(product.imageOne).into(productImageView)
                if (product.saleState) {
                    productPriceText.setBackgroundResource(R.drawable.strike_through)
                    offerPriceText.text = product.salePrice.toString()
                } else {
                    offerPriceText.visibility = View.GONE
                }

                removeImageView.setOnClickListener {
                    onCartRemoveClick(product.id, Firebase.auth.currentUser?.uid!!)
                }
                root.setOnClickListener {
                    onProductClick(product.id)
                }
            }
        }
    }

    class ProductDiffUtilCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
        }
    }
}
