package com.cabify.data.source.remote

import com.cabify.common.Either
import com.cabify.data.apiservice.IProductService
import com.cabify.data.source.remote.ProductRemoteDummy.productResponseDummy
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Test class of [ProductRemoteDataSourceImpl]
 */
@ExperimentalCoroutinesApi
class ProductRemoteDataSourceImplTest {

    private val mockWebServer = MockWebServer()

    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()

    private val api = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(IProductService::class.java)

    private val dataSource = ProductRemoteDataSourceImpl(api)

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Given product When getProduct is called Then the service return ProductResponse with code 200`() {
        mockWebServer.enqueueResponse("mock_product.json", 200)
        runBlocking {
            //Given
            val result = productResponseDummy().toEiterRight()

            //When
            val response = dataSource.getProduct()

            //Verify
            assert(response is Either.Right)
            assertEquals(result, response)
        }
    }
}