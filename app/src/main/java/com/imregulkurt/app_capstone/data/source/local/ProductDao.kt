package com.imregulkurt.app_capstone.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.imregulkurt.app_capstone.data.model.response.ProductEntity

@Dao
interface ProductDao {

    @Query("SELECT * FROM fav_products")
    suspend fun getFavProducts(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavProduct(productEntity: ProductEntity)

    @Delete
    suspend fun removeFavProduct(productEntity: ProductEntity)

    // TODO: May not be necessary?
    @Query("SELECT productId FROM fav_products")
    suspend fun getFavProductIds(): List<Int>

    @Query("DELETE FROM fav_products")
    suspend fun removeAllFavProducts()

}
