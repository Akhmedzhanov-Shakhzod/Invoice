package com.example.invoice.ui.helper.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.invoice.R
import com.example.invoice.ui.helper.items.Client

class ClientAdapter(private val context: Activity, private val clients: List<Client>):
    ArrayAdapter<Client>(context, R.layout.client_item, clients) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.client_item,null)

        val client_name: TextView = view.findViewById(R.id.client_name)
        val client_address: TextView = view.findViewById(R.id.client_address)

        client_name.text = clients[position].name
        client_address.text = clients[position].address
        return view
    }

}