package com.example.invoice.ui.helper.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.invoice.ui.helper.items.Client
import com.example.invoice.ui.helper.items.Order
import com.example.invoice.ui.helper.items.OrderProduct
import com.example.invoice.ui.helper.items.Product

class DbManager(context: Context) {
    private val dbHelper = DbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb(){
        db = dbHelper.writableDatabase
    }
    fun closeDb(){
        dbHelper.close()
    }
    fun insertClientToDb(client: Client): Int {
        val clients = readFromClient

        while (clients.moveToNext()) {
            if(clients.getString(3) == client.inn){
                return 1
            }
        }
        val values = ContentValues().apply {
            put(MyDbNameClass.Client.COLUMN_NAME_CLIENT_NAME,client.name)
            put(MyDbNameClass.Client.COLUMN_NAME_CLIENT_HOST,client.host)
            put(MyDbNameClass.Client.COLUMN_NAME_CLIENT_INN,client.inn)
            put(MyDbNameClass.Client.COLUMN_NAME_CLIENT_ADDRESS,client.address)
        }

        val success = db?.insert(MyDbNameClass.Client.TABLE_NAME_CLIENT,null,values)
        return if(Integer.parseInt("$success") != -1) 0 else 2
    }
//    fun clearClients(): Boolean {
//        val success = db!!.delete(MyDbNameClass.Client.TABLE_NAME_CLIENT, null,null)
//        return Integer.parseInt("$success") != -1
//    }
    fun insertProductToDb(product: Product): Int {
        val products = readFromProduct

        while (products.moveToNext()) {
            if(products.getString(1) == product.name && products.getString(2) == product.price.toString()){
                return 1
            }
        }
        val values = ContentValues().apply {
            put(MyDbNameClass.Product.COLUMN_NAME_PRODUCT_NAME,product.name)
            put(MyDbNameClass.Product.COLUMN_NAME_PRODUCT_PRICE,product.price)
        }
        val success = db?.insert(MyDbNameClass.Product.TABLE_NAME_PRODUCT,null,values)
        return if(Integer.parseInt("$success") != -1) 0 else 2
    }
    fun insertOrderToDb(order: Order): Int {
        val orders = readFromOrders

        while (orders.moveToNext()) {
            if(orders.getString(0).toInt() == order.orderId){
                return 1
            }
        }
        val values = ContentValues().apply {
            put(MyDbNameClass.Orders.COLUMN_NAME_ORDER_ID,order.orderId)
            put(MyDbNameClass.Orders.COLUMN_NAME_ORDER_DATE,order.orderDate)
            put(MyDbNameClass.Orders.COLUMN_NAME_ORDER_CLIENT_ID,order.orderClientId)
            put(MyDbNameClass.Orders.COLUMN_NAME_ORDER_AMOUNT,order.amount)
        }
        val success = db?.insert(MyDbNameClass.Orders.TABLE_NAME_ORDERS,null,values)
        return if(Integer.parseInt("$success") != -1) 0 else 2
    }
    fun insertOrderProductToDb(orderProduct: OrderProduct): Int {
        val orderProducts = readFromOrderProducts

        while (orderProducts.moveToNext()) {
            if(orderProducts.getString(0).toInt() == orderProduct.orderProductsId){
                return 1
            }
        }
        val values = ContentValues().apply {
            put(MyDbNameClass.OrderProducts.COLUMN_NAME_ORDER_ID,orderProduct.orderId)
            put(MyDbNameClass.OrderProducts.COLUMN_NAME_PRODUCT_NAME,orderProduct.product.name)
            put(MyDbNameClass.OrderProducts.COLUMN_NAME_PRODUCT_PRICE,orderProduct.product.price)
            put(MyDbNameClass.OrderProducts.COLUMN_NAME_PRODUCT_COUNT,orderProduct.productCount)
        }
        val success = db?.insert(MyDbNameClass.OrderProducts.TABLE_NAME_ORDER_PRODUCTS,null,values)
        return if(Integer.parseInt("$success") != -1) 0 else 2
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    fun getProductId(product: Product): Int {
        val products = readFromProduct

        while (products.moveToNext()) {
            if(products.getString(1) == product.name &&
                products.getString(2).toDouble() == product.price){
                return products.getInt(0)
            }
        }
        return -1
    }
    fun getOrderId(order: Order): Int {
        val orders = readFromOrders

        while (orders.moveToNext()) {
            if(orders.getString(1) == order.orderDate &&
                orders.getInt(2) == order.orderClientId &&
                orders.getString(3).toDouble() == order.amount){
                return orders.getInt(0)
            }
        }
        return -1
    }
    ///////////////////////////////////////////////////////////////////////////////////////////
    fun deleteClient(clientInt: Int): Boolean {
        val success = db!!.delete(MyDbNameClass.Client.TABLE_NAME_CLIENT, MyDbNameClass.Client.COLUMN_NAME_CLIENT_ID + "=?", arrayOf(clientInt.toString()))
        return Integer.parseInt("$success") != -1
    }
    fun deleteProduct(productInt: Int): Boolean {
        val success = db!!.delete(MyDbNameClass.Product.TABLE_NAME_PRODUCT, MyDbNameClass.Product.COLUMN_NAME_PRODUCT_ID + "=?", arrayOf(productInt.toString()))
        return Integer.parseInt("$success") != -1
    }
    fun deleteOrders(orderId: Int): Boolean {
        val success = db!!.delete(MyDbNameClass.Orders.TABLE_NAME_ORDERS, MyDbNameClass.Orders.COLUMN_NAME_ORDER_ID + "=?", arrayOf(orderId.toString()))
        return Integer.parseInt("$success") != -1
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    fun updateClient(oldClient: Client, newClient: Client): Boolean{
        val success2: Boolean = deleteClient(oldClient.clientId!!)
        val success1 = insertClientToDb(newClient)
        return success1 == 0 && success2
    }
    fun updateProduct(oldProduct: Product, newProduct: Product): Boolean{
        val success2: Boolean = deleteProduct(oldProduct.productId!!)
        val success1 = insertProductToDb(newProduct)
        return success1 == 0 && success2
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    fun getOrderProducts(orderId: Int): ArrayList<OrderProduct> {
        val allOP = readFromOrderProducts
        val op = ArrayList<OrderProduct>()

        while (allOP.moveToNext()){
            if(allOP.getInt(1) == orderId){
                val columns = Array<String?>(3) { null }
                columns[0] = MyDbNameClass.Product.COLUMN_NAME_PRODUCT_ID
                columns[1] = MyDbNameClass.Product.COLUMN_NAME_PRODUCT_NAME
                columns[2] = MyDbNameClass.Product.COLUMN_NAME_PRODUCT_PRICE

                val tempProduct = Product(allOP.getString(2),allOP.getDouble(3))
                val tempOp = OrderProduct(allOP.getInt(1),tempProduct,allOP.getDouble(4),allOP.getInt(0))
                op.add(tempOp)
            }
        }
        return op
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    val readFromClient : Cursor
        get() {
            val res = db!!.rawQuery("SELECT * FROM " + MyDbNameClass.Client.TABLE_NAME_CLIENT +
                    " ORDER BY " + MyDbNameClass.Client.COLUMN_NAME_CLIENT_NAME, null)
            return res
        }
    val readFromProduct : Cursor
        get() {
            val res = db!!.rawQuery("SELECT * FROM " + MyDbNameClass.Product.TABLE_NAME_PRODUCT +
                    " ORDER BY " + MyDbNameClass.Product.COLUMN_NAME_PRODUCT_NAME, null)
            return res
        }
    val readFromOrders : Cursor
        get() {
            val res = db!!.rawQuery("SELECT * FROM " + MyDbNameClass.Orders.TABLE_NAME_ORDERS +
                    " ORDER BY " + MyDbNameClass.Orders.COLUMN_NAME_ORDER_ID + " DESC", null)
            return res
        }
    private val readFromOrderProducts : Cursor
        get() {
            val res = db!!.rawQuery("SELECT * FROM " + MyDbNameClass.OrderProducts.TABLE_NAME_ORDER_PRODUCTS +
                    " ORDER BY " + MyDbNameClass.OrderProducts.COLUMN_NAME_ORDER_ID + " DESC", null)
            return res
        }

    ///////////////////////////////////////////////////////////////////////////////////////////
    fun clearOrders(): Boolean {
        val success = db!!.delete(MyDbNameClass.Orders.TABLE_NAME_ORDERS, null,null)
        return Integer.parseInt("$success") != -1
    }
    fun clearOrderProducts(): Boolean {
        val success = db!!.delete(MyDbNameClass.OrderProducts.TABLE_NAME_ORDER_PRODUCTS, null,null)
        return Integer.parseInt("$success") != -1
    }
}