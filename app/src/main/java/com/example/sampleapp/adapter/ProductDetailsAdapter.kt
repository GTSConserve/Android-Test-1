package com.example.sampleapp.adapter

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.sampleapp.R
import com.example.sampleapp.model.response.Details
import kotlinx.android.synthetic.main.product_details_list_layout.view.*


/*
*
* adapter class to set the adapter value for the product details listview
* */
class ProductDetailsAdapter(
        val context: Context,
        var detailsList: ArrayList<Details?>? = ArrayList(),
        val clickListener: (Details) -> Unit
) : RecyclerView.Adapter<ProductDetailsAdapter.ProfileViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val layout = LayoutInflater.from(parent.context)
                .inflate(R.layout.product_details_list_layout, parent, false)
        return ProfileViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return if (detailsList.isNullOrEmpty())
            0
        else
            detailsList?.size!!
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val value = detailsList?.get(position)
        value?.let { holder.bind(it, clickListener, context) }
    }


    class ProfileViewHolder(val itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bind(
                details: Details,
                clickListener: (Details) -> Unit,
                context: Context
        ) {
            val value = details.price
            val title = details.product_name
            itemview.product_name_txt.text = title
            itemview.price_txt_view.text = value
            itemview.product_imageview.load(details.img_url) {
                crossfade(true)
                placeholder(R.drawable.image_placeholder)
                transformations(RoundedCornersTransformation())
            }
            //veg
            if (details.type.equals("1"))
                itemview.food_type.setImageDrawable(itemview.context.getDrawable(R.drawable.ic_veg))
            else if ((details.type.equals("0")))
                itemview.food_type.setImageDrawable(itemview.context.getDrawable(R.drawable.ic_non_veg))

            val values = ArrayList<String>()
            values.add("Variant")
            //Create the object for ArrayAdapter

            val adapter = ArrayAdapter(itemview.context, R.layout.spinner_layout, R.id.state_spinner_text, values)
            itemview.variant_selecter.adapter = adapter

            if (details.variation.equals("false")) {

                val length = itemview.price_txt_view.text.length
                itemview.price_txt_view.setText(details.price + " " + details.demoamt, TextView.BufferType.SPANNABLE)
                val spannable = itemview.price_txt_view.text as Spannable
                spannable.setSpan(StrikethroughSpan(), length + 1, spannable.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                spannable.setSpan(
                        ForegroundColorSpan(Color.parseColor("#8A2D50")),
                        length + 1, spannable.length,
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )
                spannable.setSpan(AbsoluteSizeSpan(13, true), length, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            } else if (details.variation.equals("true")) {
                //Todo am not sure about the values in the description


            }
            //set the max value to avaiable item count
            itemview.quantityButton.maxQuantity = details.available?.toInt() ?: 0

            itemview.fav_image_view.tag = false
            itemview.fav_image_view.setOnClickListener {
                if (it?.tag == false) {
                    it.setBackgroundResource(R.drawable.ic_fav_selected)
                    it.tag = true
                } else {
                    it.setBackgroundResource(R.drawable.ic_fav_unselect)
                    it?.tag = false
                }
            }
            itemview.setOnClickListener {
                clickListener(details)
            }

        }

    }
}
