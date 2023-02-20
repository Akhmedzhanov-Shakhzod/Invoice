package com.example.invoice.ui.helper.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.invoice.R
import com.example.invoice.ui.helper.MAIN
import com.example.invoice.ui.helper.db.DbManager
import com.example.invoice.ui.helper.items.Order

class OrderAdapter(private val context: Activity, private val order: ArrayList<Order>):
    ArrayAdapter<Order>(context, R.layout.order_item, order) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.order_item,null)

        val order_id: TextView = view.findViewById(R.id.order_id)
        val order_client: TextView = view.findViewById(R.id.order_client_name)
        val order_date: TextView = view.findViewById(R.id.order_date)
        val order_amount: TextView = view.findViewById(R.id.order_amount)

        val dbManager = DbManager(MAIN)
        dbManager.openDb()

        val clients = dbManager.readFromClient
        while (clients.moveToNext()) {
            if(clients.getInt(0) == order[position].orderClientId){
                order_client.text = clients.getString(1)
                break
            }
        }
        order_id.text = "№" + order[position].orderId.toString()
        order_date.text = order[position].orderDate
        order_amount.text = order[position].amount.toString()

        dbManager.closeDb()
        return view
    }
}