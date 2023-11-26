package com.imregulkurt.app_capstone.data.model.response

data class ProductUI(
    val id: Int,
    val category: String,
    val title: String,
    val description: String,
    val imageOne: String,
    val count: Int,
    val price: Double,
    val rate: Double,
    val salePrice: Double,
    val saleState: Boolean,
    var favState: Boolean,
)
