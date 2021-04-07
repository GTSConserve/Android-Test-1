package com.example.sampleapp.model.response

import com.google.gson.annotations.SerializedName

class ProductDetails {

    @SerializedName("Details")
    var DetailsList: ArrayList<Details?>? = null
    var total: String? = null
    var status: String? = null
    var message: String? = null

}
