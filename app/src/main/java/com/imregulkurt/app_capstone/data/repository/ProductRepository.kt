package com.imregulkurt.app_capstone.data.repository

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.imregulkurt.app_capstone.MainApplication
import com.imregulkurt.app_capstone.common.Resource
import com.imregulkurt.app_capstone.common.getFavProductsStatic
import com.imregulkurt.app_capstone.data.mapper.mapProductToProductUI
import com.imregulkurt.app_capstone.data.mapper.mapProductUIToProductEntity
import com.imregulkurt.app_capstone.data.mapper.mapProductEntityToProductUI
import com.imregulkurt.app_capstone.data.model.request.AddToCartRequest
import com.imregulkurt.app_capstone.data.model.request.ClearCartRequest
import com.imregulkurt.app_capstone.data.model.request.DeleteFromCartRequest
import com.imregulkurt.app_capstone.data.model.response.BaseResponse
import com.imregulkurt.app_capstone.data.model.response.Product
import com.imregulkurt.app_capstone.data.model.response.ProductUI
import com.imregulkurt.app_capstone.data.source.local.ProductDao
import com.imregulkurt.app_capstone.data.source.remote.ProductService
import com.imregulkurt.app_capstone.di.RepositoryModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository(private val productService: ProductService, private val productDao: ProductDao) {

    //region Locals

    suspend fun getFavProducts(): Resource<List<ProductUI>> =
        withContext(Dispatchers.IO) {
        try {
            val favProducts = productDao.getFavProducts()
            if (favProducts.isNotEmpty()) {
                val resultList = mutableListOf<ProductUI>()
                for (productEntity in favProducts) {
                    if (productEntity.favState == true){
                        resultList.add(productEntity.mapProductEntityToProductUI(true))
                    }
                }

                // TODO: temporary for debug, can be removed
                val message = "Successfully obtained favorite products"
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(MainApplication.appContext, message, Toast.LENGTH_SHORT).show()
                }
                Resource.Success(resultList)
            } else {

                // TODO: temporary for debug, can be removed
                val message = "Failed to obtain favorite products"
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(MainApplication.appContext, message, Toast.LENGTH_SHORT).show()
                }
                Resource.Fail("Something went wrong!")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.orEmpty())
        }
    }

    suspend fun addToFavorites(productUI: ProductUI) {
        productDao.addFavProduct(productUI.mapProductUIToProductEntity(true))
    }

    suspend fun removeFromFavorites(productUI: ProductUI) {
        productDao.removeFavProduct(productUI.mapProductUIToProductEntity(false))
    }

    suspend fun clearAllFavorites() {
        productDao.removeAllFavProducts()
    }

    //endregion

    //region Remotes

    suspend fun getAllProducts(): Resource<List<ProductUI>> =
        withContext(Dispatchers.IO) {
            try {
                val favProductIds = productDao.getFavProductIds()
                val response = productService.getProducts().body()
                if (response?.status == 200) {
                    val resultList = mutableListOf<ProductUI>()
                    for (product in response.products!!) {
                        resultList.add(product.mapProductToProductUI(favProductIds.contains(product.id)))
                    }

                    // TODO: temporary for debug, can be removed
                    val message = "Successfully obtained all products"
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(MainApplication.appContext, message, Toast.LENGTH_SHORT).show()
                    }
                    Resource.Success(resultList)
                } else {

                    // TODO: temporary for debug, can be removed
                    val message = "Failed to obtain all products"
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(MainApplication.appContext, message, Toast.LENGTH_SHORT).show()
                    }
                    Resource.Fail("Something went wrong!")
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun getProductDetail(productId: Int): Resource<ProductUI> =
        withContext(Dispatchers.IO) {
            try {
                val favProductIds = productDao.getFavProductIds()
                val response = productService.getProductDetail(productId).body()
                if (response?.status == 200) {

                    // TODO: temporary for debug, can be removed
                    val message = "Successfully obtained product details for $productId"
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(MainApplication.appContext, message, Toast.LENGTH_SHORT).show()
                    }
                    Resource.Success(response.product!!.mapProductToProductUI(favProductIds.contains(productId)))
                } else {

                    // TODO: temporary for debug, can be removed
                    val message = "Failed to obtain all products"
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(MainApplication.appContext, message, Toast.LENGTH_SHORT).show()
                    }
                    Resource.Fail("Something went wrong!")
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun getSaleProducts(): Resource<List<ProductUI>> =
        withContext(Dispatchers.IO) {
            try {
                val favProductIds = productDao.getFavProductIds()
                val response = productService.getSaleProducts().body()
                if (response?.status == 200) {
                    val resultList = mutableListOf<ProductUI>()
                    for (product in response.products!!){
                        resultList.add(product.mapProductToProductUI(favProductIds.contains(product.id)))
                    }

                    // TODO: temporary for debug, can be removed
                    val message = "Successfully obtained all sale products"
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(MainApplication.appContext, message, Toast.LENGTH_SHORT).show()
                    }
                    Resource.Success(resultList)
                } else {

                    // TODO: temporary for debug, can be removed
                    val message = "Failed to obtain all sale products"
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(MainApplication.appContext, message, Toast.LENGTH_SHORT).show()
                    }
                    Resource.Fail("Something went wrong!")
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun getSearchedProducts(query: String): Resource<List<ProductUI>> =
        withContext(Dispatchers.IO) {
            try {
                val favProductIds = productDao.getFavProductIds()
                val response = productService.getSearchProduct(query).body()
                if (response?.status == 200) {
                    val resultList = mutableListOf<ProductUI>()
                    for (product in response.products!!) {
                        resultList.add(product.mapProductToProductUI(favProductIds.contains(product.id)))
                    }

                    // TODO: temporary for debug, can be removed
                    val message = "Successfully obtained search products"
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(MainApplication.appContext, message, Toast.LENGTH_SHORT).show()
                    }
                    Resource.Success(resultList)
                } else {

                    // TODO: temporary for debug, can be removed
                    val message = "Failed to obtain search products"
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(MainApplication.appContext, message, Toast.LENGTH_SHORT).show()
                    }
                    Resource.Fail("Something went wrong!")
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun getCartProducts(query: String): Resource<List<ProductUI>> =
        withContext(Dispatchers.IO) {
            try {
                val favProductIds = productDao.getFavProductIds()
                val response = productService.getCartProduct(query).body()
                if (response?. status == 200) {
                    val resultList = mutableListOf<ProductUI>()
                    for (product in response.products!!) {
                        resultList.add(product.mapProductToProductUI(favProductIds.contains(product.id)))
                    }

                    // TODO: temporary for debug, can be removed
                    val message = "Successfully obtained cart products"
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(MainApplication.appContext, message, Toast.LENGTH_SHORT).show()
                    }
                    Resource.Success(resultList)
                } else {

                    // TODO: temporary for debug, can be removed
                    val message = "Failed to obtain cart products"
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(MainApplication.appContext, message, Toast.LENGTH_SHORT).show()
                    }
                    Resource.Fail("Something went wrong!")
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }

    //endregion

    //region Requests

    suspend fun addToCart(userId: String, productId: Int): Resource<BaseResponse> =
        withContext(Dispatchers.IO) {
            try {
                val request = AddToCartRequest(userId, productId)
                val response = productService.addToCart(request).body()
                if (response?.status == 200) {
                    Resource.Success(response)
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun removeFromCart(userId: String, productId: Int): Resource<BaseResponse> =
        withContext(Dispatchers.IO) {
            try {
                val request = DeleteFromCartRequest(userId, productId)
                val response = productService.removeFromCart(request).body()
                if (response?.status == 200) {
                    Resource.Success(response)
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun clearCart(userId: String): Resource<BaseResponse> =
        withContext(Dispatchers.IO) {
            try {
                val request = ClearCartRequest(userId)
                val response = productService.clearCart(request).body()
                if (response?.status == 200) {
                    Resource.Success(response)
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }

    //endregion

}