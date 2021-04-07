package com.example.sampleapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sampleapp.BuildConfig
import com.example.sampleapp.model.request.RequestData
import com.example.sampleapp.model.response.ProductDetails
import com.example.sampleapp.retrofit.APIService
import okhttp3.*
import okio.Buffer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


class ProductViewModel : ViewModel() {
    //fetched asynchronously
    private var productMutableLiveData: MutableLiveData<ProductDetails?>? = null

    //get the data
    fun getProductDetails(): LiveData<ProductDetails?>? {
        //null checks
        if (productMutableLiveData == null) {
            productMutableLiveData = MutableLiveData<ProductDetails?>()
            callProductInformation()
        }
        return productMutableLiveData
    }


    //get the JSON data from URL
    private fun callProductInformation() {
        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS) // .addInterceptor(interceptor)
                .build()
        val retrofit = Retrofit.Builder()
                .baseUrl(APIService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        val apiService = retrofit.create(APIService::class.java)
        val call: Call<ProductDetails?>? =
                apiService.getProductData("f834b188")
        //used the enqueue method to perform the operation in asynchronous
        call?.enqueue(object : Callback<ProductDetails?> {
            override fun onResponse(
                    call: Call<ProductDetails?>,
                    response: retrofit2.Response<ProductDetails?>
            ) {
                if (response.body() == null) return
                if (response.isSuccessful) //set list to our MutableLiveData
                    productMutableLiveData!!.value = response.body()
            }

            override fun onFailure(call: Call<ProductDetails?>?, t: Throwable) {
                t.printStackTrace()
                productMutableLiveData!!.value = null
            }

        })
    }

}