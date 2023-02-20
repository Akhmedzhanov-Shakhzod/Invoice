package com.example.invoice.ui.helper.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.invoice.R
import com.example.invoice.ui.helper.items.Product

class ProductAdapter(private val context: Activity, private val product: ArrayList<Product>):
    ArrayAdapter<Product>(context, R.layout.product_item, product) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.product_item,null)

        val product_name: TextView = view.findViewById(R.id.product_name)
        val product_price: TextView = view.findViewById(R.id.product_price)

        product_name.text = product[position].name
        product_price.text = product[position].price.toString()
        return view
    }
}