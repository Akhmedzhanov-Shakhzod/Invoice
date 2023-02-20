package com.example.invoice.ui.helper.db

object MyDbNameClass {
    const val DATABASE_VERSION = 102
    const val DATABASE_NAME = "Invoice.db"

    /************************** TABLE CLIENT **********************/

    object Client {
        const val TABLE_NAME_CLIENT = "client"
        const val COLUMN_NAME_CLIENT_ID = "client_id"
        const val COLUMN_NAME_CLIENT_NAME = "client_name"
        const val COLUMN_NAME_CLIENT_HOST = "client_host"
        const val COLUMN_NAME_CLIENT_INN = "client_inn"
        const val COLUMN_NAME_CLIENT_ADDRESS = "client_address"
    }

    const val SQL_CREATE_CLIENT =
        "CREATE TABLE IF NOT EXISTS ${Client.TABLE_NAME_CLIENT} (" +
                "${Client.COLUMN_NAME_CLIENT_ID} INTEGER PRIMARY KEY," +
                "${Client.COLUMN_NAME_CLIENT_NAME} TEXT," +
                "${Client.COLUMN_NAME_CLIENT_HOST} TEXT," +
                "${Client.COLUMN_NAME_CLIENT_INN} TEXT," +
                "${Client.COLUMN_NAME_CLIENT_ADDRESS} TEXT)"

    const val SQL_DELETE_CLIENT = "DROP TABLE IF EXISTS ${Client.TABLE_NAME_CLIENT}"

////////////////////////////////////////////////////////////////

    /*************************** TABLE PRODUCT *********************/

    object Product {
        const val TABLE_NAME_PRODUCT = "product"
        const val COLUMN_NAME_PRODUCT_ID = "product_id"
        const val COLUMN_NAME_PRODUCT_NAME = "product_name"
        const val COLUMN_NAME_PRODUCT_PRICE = "product_price"
    }

    const val SQL_CREATE_PRODUCT =
        "CREATE TABLE IF NOT EXISTS ${Product.TABLE_NAME_PRODUCT} (" +
                "${Product.COLUMN_NAME_PRODUCT_ID} INTEGER PRIMARY KEY," +
                "${Product.COLUMN_NAME_PRODUCT_NAME} TEXT," +
                "${Product.COLUMN_NAME_PRODUCT_PRICE} REAL)"

    const val SQL_DELETE_PRODUCT = "DROP TABLE IF EXISTS ${Product.TABLE_NAME_PRODUCT}"

/////////////////////////////////////////////////////////////////////

    /*************************** TABLE ORDER *********************/

    object Orders {
        const val TABLE_NAME_ORDERS = "orders"
        const val COLUMN_NAME_ORDER_ID = "order_id"
        const val COLUMN_NAME_ORDER_DATE = "order_date"
        const val COLUMN_NAME_ORDER_CLIENT_ID = "order_client_id"
        const val COLUMN_NAME_ORDER_AMOUNT = "order_amount"
    }

    const val SQL_CREATE_ORDERS =
        "CREATE TABLE IF NOT EXISTS ${Orders.TABLE_NAME_ORDERS} (" +
                "${Orders.COLUMN_NAME_ORDER_ID} INTEGER PRIMARY KEY," +
                "${Orders.COLUMN_NAME_ORDER_DATE} TEXT," +
                "${Orders.COLUMN_NAME_ORDER_CLIENT_ID} INTEGER," +
                "${Orders.COLUMN_NAME_ORDER_AMOUNT} REAL)"

    const val SQL_DELETE_ORDERS = "DROP TABLE IF EXISTS ${Orders.TABLE_NAME_ORDERS}"

    object OrderProducts {
        const val TABLE_NAME_ORDER_PRODUCTS = "order_products"
        const val COLUMN_NAME_ORDER_PRODUCTS_ID = "order_products_id"
        const val COLUMN_NAME_ORDER_ID = "order_id"
        const val COLUMN_NAME_PRODUCT_NAME = "product_name"
        const val COLUMN_NAME_PRODUCT_PRICE = "product_price"
        const val COLUMN_NAME_PRODUCT_COUNT = "product_count"
    }

    const val SQL_CREATE_ORDER_PRODUCTS =
        "CREATE TABLE IF NOT EXISTS ${OrderProducts.TABLE_NAME_ORDER_PRODUCTS} (" +
                "${OrderProducts.COLUMN_NAME_ORDER_PRODUCTS_ID} INTEGER PRIMARY KEY," +
                "${OrderProducts.COLUMN_NAME_ORDER_ID} INTEGER," +
                "${OrderProducts.COLUMN_NAME_PRODUCT_NAME} TEXT," +
                "${OrderProducts.COLUMN_NAME_PRODUCT_PRICE} REAL," +
                "${OrderProducts.COLUMN_NAME_PRODUCT_COUNT} REAL)"

    const val SQL_DELETE_ORDER_PRODUCTS = "DROP TABLE IF EXISTS ${OrderProducts.TABLE_NAME_ORDER_PRODUCTS}"

/////////////////////////////////////////////////////////////////
}