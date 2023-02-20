package com.example.invoice.ui.helper.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.invoice.R
import com.example.invoice.ui.helper.items.Product

class ChoseProductAdapter(private val context: Activity, private val product: ArrayList<Pair<Product,Double>>):
    ArrayAdapter<Pair<Product, Double>>(context, R.layout.chosen_product_item, product) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.chosen_product_item,null)

        val product_name: TextView = view.findViewById(R.id.chosen_product_name)
        val product_price: TextView = view.findViewById(R.id.chosen_product_price)
        val product_count: TextView = view.findViewById(R.id.chosen_product_count)
        val product_amount: TextView = view.findViewById(R.id.chosen_product_amount)

        product_name.text = product[position].first.name
        product_price.text = product[position].first.price.toString()
        product_count.text = product[position].second.toString()

        val amount = product[position].first.price * product[position].second
        product_amount.text = amount.toString()
        return view
    }
}