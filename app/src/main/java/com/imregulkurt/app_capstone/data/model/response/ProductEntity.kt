package com.imregulkurt.app_capstone.data.model.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_products")
data class ProductEntity(
    @PrimaryKey
    @ColumnInfo(name = "productId")
    val id: Int?,

    @ColumnInfo(name = "category")
    val category: String?,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "description")
    val description: String?,

    // TODO: check for ColumnInfo (may rename it to image since there is only one)
    @ColumnInfo(name = "imageOne")
    val imageOne: String?,

    @ColumnInfo(name = "count")
    val count: Int?,

    @ColumnInfo(name = "price")
    val price: Double?,

    @ColumnInfo(name = "rate")
    val rate: Double?,

    @ColumnInfo(name = "salePrice")
    val salePrice: Double?,

    @ColumnInfo(name = "saleState")
    val saleState: Boolean?,

    @ColumnInfo(name = "favState")
    val favState: Boolean?,
)
