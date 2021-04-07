package com.example.sampleapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sampleapp.adapter.ProductDetailsAdapter
import com.example.sampleapp.model.response.Details
import com.example.sampleapp.model.response.ProductDetails
import com.example.sampleapp.util.GeneralMethods
import com.example.sampleapp.viewmodel.ProductViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var model: ProductViewModel? = null
    var adapter: ProductDetailsAdapter? = null
    var productDetailsList: ArrayList<Details?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        model = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        setAdapter()
        if (GeneralMethods.isNetworkAvailable(this)) {
            myProductDetailsModel()
        } else {
            noDataGroup.visibility = View.VISIBLE
            product_details_list.visibility = View.GONE
            Snackbar
                    .make(mainlayout, getString(R.string.noInternet), Snackbar.LENGTH_LONG).show()
        }

    }

    private fun setAdapter() {
        product_details_list.setHasFixedSize(true)
        product_details_list.itemAnimator = DefaultItemAnimator()
        product_details_list.layoutManager = LinearLayoutManager(this)
    }

    fun myProductDetailsModel() {
        model?.getProductDetails()?.observe(this, Observer<ProductDetails?> {
            productDetailsList = it?.DetailsList
            if (adapter == null) {
                adapter = ProductDetailsAdapter(this@MainActivity, productDetailsList) { value -> itemClicked(value) }
                product_details_list.adapter = adapter
                product_details_list.isNestedScrollingEnabled = true
            } else {
                adapter?.detailsList = productDetailsList
                adapter!!.notifyDataSetChanged()
            }
        })
    }

    /*
    *method to set the click listener for the list item
    *
    * */
    private fun itemClicked(value: Details) {
        //open the set status activity to update/set the status for the logged in user
        Toast.makeText(this,value.product_name.toString(),Toast.LENGTH_LONG).show()

    }
}



