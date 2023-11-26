package com.imregulkurt.app_capstone.data.mapper

import com.imregulkurt.app_capstone.data.model.response.Product
import com.imregulkurt.app_capstone.data.model.response.ProductEntity
import com.imregulkurt.app_capstone.data.model.response.ProductUI
import com.imregulkurt.app_capstone.di.RepositoryModule

fun ProductUI.mapProductUIToProductEntity(isFavorite: Boolean): ProductEntity {
    return ProductEntity(
        id = id,
        category = category,
        title = title,
        description = description,
        imageOne = imageOne,
        count = count,
        price = price,
        rate = rate,
        salePrice = salePrice,
        saleState = saleState,
        // favState = favState,
        favState = isFavorite,
    )
}

fun Product.mapProductToProductUI(isFavorite: Boolean): ProductUI {
    return ProductUI(
        id = id ?: -1,
        category = category ?: "",
        title = title ?: "",
        description = description ?: "",
        imageOne = imageOne ?: "",
        count = count ?: -1,
        price = price ?: -1.0,
        rate = rate ?: -1.0,
        salePrice = salePrice ?: -1.0,
        saleState = saleState ?: false,
        // favState = favState ?: false,
        favState = isFavorite,
    )
}

fun ProductEntity.mapProductEntityToProductUI(isFavorite: Boolean): ProductUI {
    return ProductUI(
        id = id ?: -1,
        category = category ?: "",
        title = title ?: "",
        description = description ?: "",
        imageOne = imageOne ?: "",
        count = count ?: -1,
        price = price ?: -1.0,
        rate = rate ?: -1.0,
        salePrice = salePrice ?: -1.0,
        saleState = saleState ?: false,
        // favState = favState ?: false,
        favState = isFavorite,
    )
}
