package com.cabify.data.apiservice

import com.cabify.common.Constants.PRODUCT
import com.cabify.data.response.product.ProductResponse
import retrofit2.Response
import retrofit2.http.GET

interface IProductService {
    @GET(PRODUCT)
    suspend fun getProduct(): Response<ProductResponse>
}
