package com.example.invoice.ui.helper.items

data class Order(var orderDate: String, var orderClientId: Int, var amount:Double, var orderId: Int? = null)
