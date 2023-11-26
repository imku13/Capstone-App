package com.imregulkurt.app_capstone.data.source.remote

import com.imregulkurt.app_capstone.data.model.request.AddToCartRequest
import com.imregulkurt.app_capstone.data.model.request.ClearCartRequest
import com.imregulkurt.app_capstone.data.model.request.DeleteFromCartRequest
import com.imregulkurt.app_capstone.data.model.response.ProductDetailResponse
import com.imregulkurt.app_capstone.data.model.response.ProductResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ProductService {

    @GET("get_products.php")
    suspend fun getProducts(
    ): Response<ProductResponse>

    @GET("get_product_detail.php")
    suspend fun getProductDetail(
        @Query("id") id: Int
    ): Response<ProductDetailResponse>

    @GET("get_sale_products.php")
    suspend fun getSaleProducts(
    ): Response<ProductResponse>

    @GET("get_products_by_category.php")
    suspend fun getProductsByCategory(
        @Query("category") categoryValue: String
    ): Response<ProductResponse>

    @GET("search_product.php")
    suspend fun getSearchProduct(
        @Query("query") queryValue: String
    ): Response<ProductResponse>

    @GET("get_cart_products.php")
    suspend fun getCartProduct(
        @Query("userId") userId: String
    ): Response<ProductResponse>

    @POST("add_to_cart.php")
    suspend fun addToCart(
        @Body request: AddToCartRequest
    ): Response<ProductDetailResponse>

    @POST("delete_from_cart.php")
    suspend fun removeFromCart(
        @Body request: DeleteFromCartRequest
    ): Response<ProductResponse>

    @POST("clear_cart.php")
    suspend fun clearCart(
        @Body request: ClearCartRequest
    ): Response<ProductResponse>
}
