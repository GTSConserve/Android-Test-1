package com.example.sampleapp.retrofit

import com.example.sampleapp.model.request.RequestData
import com.example.sampleapp.model.response.ProductDetails
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface APIService {
    @GET
    fun getProductData(
            @Url url: String?): Call<ProductDetails?>?

    companion object {
        const val BASE_URL = "https://api.mocki.io/v1/"
    }
}