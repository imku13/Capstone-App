package com.imregulkurt.app_capstone.ui.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imregulkurt.app_capstone.R
import com.imregulkurt.app_capstone.data.model.response.ProductUI
import com.imregulkurt.app_capstone.databinding.ItemFavoriteBinding

class FavoriteAdapter(private val onProductClick: (Int) -> Unit, private val onFavoriteRemoveClick: (ProductUI) -> Unit): ListAdapter<ProductUI, FavoriteAdapter.FavoriteProductViewHolder>(ProductDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteProductViewHolder {
        return FavoriteProductViewHolder(
            ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onProductClick,
            onFavoriteRemoveClick
        )
    }

    override fun onBindViewHolder(holder: FavoriteProductViewHolder, position: Int) =
        holder.bind(getItem(position))

    class FavoriteProductViewHolder(
        private val binding: ItemFavoriteBinding,
        private val onProductClick: (Int) -> Unit,
        private val onFavoriteRemoveClick: (ProductUI) -> Unit
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
                    onFavoriteRemoveClick(product)
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
