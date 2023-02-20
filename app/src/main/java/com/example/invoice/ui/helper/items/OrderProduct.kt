package com.example.invoice.ui.helper.items

data class OrderProduct(var orderId: Int, var product: Product, var productCount:Double,
                        var orderProductsId: Int? = null, var productAmount: Double = product.price * productCount )
