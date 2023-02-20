package com.example.invoice.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.invoice.R
import com.example.invoice.ui.helper.CHOSENPRODUCTSDATAMODEL
import com.example.invoice.ui.helper.items.Product

class ProductFromAddOrderBlankFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_from_add_order_blank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product_name = view.findViewById<TextView>(R.id.product_name_add_order)
        val product_price = view.findViewById<TextView>(R.id.product_price_add_order)
        val product_count = view.findViewById<EditText>(R.id.product_count_add_order)
        val product_amount = view.findViewById<TextView>(R.id.product_amount_add_order)
        val coninue = view.findViewById<Button>(R.id.save_count_product_add_order)
        val clearProduct = view.findViewById<Button>(R.id.clear_product_add_order)

        val name = arguments?.getString("product_name").toString()
        val price = arguments?.getString("product_price").toString()
        var count:Double = arguments?.getString("product_count").toString().toDouble()

        product_name.text = name
        product_price.text = price
        product_count.setText(count.toString())

        product_count.isSelected = true

        var amount:Double = price.toDouble() * product_count.text.toString().toDouble()

        product_amount.text = amount.toString()

        product_count.addTextChangedListener {
            if (it != null && it.isNotEmpty()) {
                count = it.toString().toDouble()
                amount = price.toDouble() * count
                product_amount.text = amount.toString()
            }
        }

        coninue.setOnClickListener {
            for(i in 0 until CHOSENPRODUCTSDATAMODEL.product.count()){
                if(CHOSENPRODUCTSDATAMODEL.product[i].first.name == name){
                    val product = Product(name,price.toDouble())
                    val pair = Pair(product,count)
                    CHOSENPRODUCTSDATAMODEL.productAmount[i] = amount
                    CHOSENPRODUCTSDATAMODEL.product[i] = pair
                }
            }
            findNavController().navigate(R.id.nav_home)
        }

        clearProduct.setOnClickListener {
            for(i in 0 until CHOSENPRODUCTSDATAMODEL.product.count()){
                if(CHOSENPRODUCTSDATAMODEL.product[i].first.name == name)
                    CHOSENPRODUCTSDATAMODEL.productAmount[i] = 0.0
            }
            CHOSENPRODUCTSDATAMODEL.product.removeIf {
                it.first.name == name
            }
            CHOSENPRODUCTSDATAMODEL.productAmount.removeIf {
                it == 0.0
            }
            findNavController().navigate(R.id.nav_home)
        }
    }

}