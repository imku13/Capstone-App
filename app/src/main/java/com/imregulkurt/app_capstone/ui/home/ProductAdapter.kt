package com.imregulkurt.app_capstone.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imregulkurt.app_capstone.R
import com.imregulkurt.app_capstone.data.model.response.ProductUI
import com.imregulkurt.app_capstone.databinding.ItemProductBinding

class ProductsAdapter(private val onProductClick: (Int) -> Unit, private val onFavoriteClick: (ProductUI) -> Unit): ListAdapter<ProductUI, ProductsAdapter.ProductViewHolder>(ProductDiffUtilCallBack()) {

    // Class for vertical recycler view (All Products)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onProductClick,
            onFavoriteClick
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(getItem(position))

    class ProductViewHolder(private val binding: ItemProductBinding, private val onProductClick: (Int) -> Unit, private val onFavoriteClick: (ProductUI) -> Unit): RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) {
            with(binding) {
                productText.text = product.title
                productPriceText.text = product.price.toString()

                // example: https://i.ibb.co/vdgGk43/milwaukee-drilling-hammer-3lb-fiberglass-48-22-9310.jpg
                Glide.with(productImageView).load(product.imageOne).into(productImageView)

                if (product.saleState) {
                    productPriceText.setBackgroundResource(R.drawable.strike_through)
                    offerPriceText.text = product.salePrice.toString()
                } else {
                    offerPriceText.visibility = View.GONE
                }

                favImageView.setBackgroundResource(
                    if (product.favState) {
                        R.drawable.ic_favorite
                    } else {
                        R.drawable.ic_unfavorite
                    }
                )

                root.setOnClickListener {
                    onProductClick(product.id)
                }

                favImageView.setOnClickListener {
                    onFavoriteClick(product)
                    product.favState = !product.favState
                    favImageView.setBackgroundResource(
                        if (product.favState) {
                            R.drawable.ic_favorite
                        } else {
                            R.drawable.ic_unfavorite
                        }
                    )
                }
            }
        }
    }

    // TODO: ask???
    class ProductDiffUtilCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
        }
    }
}