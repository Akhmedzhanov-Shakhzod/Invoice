package com.example.invoice.ui.home

import com.example.invoice.ui.helper.items.Product

class DataModel {
    var clientId: Int? = null
    var clientName: String = ""
    var clientAddress: String = ""
    val product = ArrayList<Pair<Product,Double>>()
    val productAmount = ArrayList<Double>()
}