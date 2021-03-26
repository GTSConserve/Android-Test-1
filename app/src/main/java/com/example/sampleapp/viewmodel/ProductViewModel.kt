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
        val requestData = RequestData("24", "")
        val call: Call<ProductDetails?>? =
                apiService.getProductData("get_all_products.php", requestData)
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


    private val interceptor: Interceptor = Interceptor { chain ->
        val originalRequest: Request = chain.request() //Current Request
        var response: Response = chain.proceed(
                originalRequest
        ) //Get response of the request
        if (BuildConfig.DEBUG) {
            //logging the response
            val bodyString = response.body()!!.string()
            Log.e(
                    "RETROFIT", """
     Sending request %s with headers 
     %s
     Input %s
     Response HTTP %s %s 
     Body %s
     ${originalRequest.url()}${originalRequest.headers()}${bodyToString(originalRequest)}${response.code()}${response.message()}$bodyString
     """.trimIndent()
            )
            response = response.newBuilder().body(
                    ResponseBody.create(response.body()!!.contentType(), bodyString)
            ).build()
        }
        response
    }

    private fun bodyToString(request: Request): String {
        var body = ""
        return try {
            val buffer = Buffer()
            if (request.body() != null) {
                request.body()!!.writeTo(buffer)
                body = buffer.readUtf8()
            }
            body
        } catch (e: IOException) {
            "Not Working"
        }
    }

}