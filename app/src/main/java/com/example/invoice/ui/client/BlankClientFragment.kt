package com.example.invoice.ui.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.invoice.R
import com.example.invoice.ui.helper.MAIN
import com.example.invoice.ui.helper.db.DbManager
import com.example.invoice.ui.helper.items.Client

class BlankClientFragment : Fragment() {

    private val dbManager = DbManager(MAIN)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank_client, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbManager.openDb()

        val delete = view.findViewById<Button>(R.id.delete_client)
        val save = view.findViewById<Button>(R.id.save_client)

        val name = view.findViewById<EditText>(R.id.client_name)
        val host = view.findViewById<EditText>(R.id.client_host)
        val inn = view.findViewById<EditText>(R.id.client_inn)
        val address = view.findViewById<EditText>(R.id.client_address)

        val clientIdText = arguments?.getString("client_id").toString()
        val clientNameText = arguments?.getString("client_name").toString()
        val clientHostText = arguments?.getString("client_host").toString()
        val clientInnText = arguments?.getString("client_inn").toString()
        val clientAddressText = arguments?.getString("client_address").toString()
        name.setText(clientNameText)
        host.setText(clientHostText)
        inn.setText(clientInnText)
        address.setText(clientAddressText)

        delete.setOnClickListener {
            if(dbManager.deleteClient(clientIdText.toInt())){
                MAIN.alert("${clientNameText} - удалено",1000)
                MAIN.onClient()
            }
            else {
                MAIN.alert("Не получилось удалить ${arguments?.getString("client_name")}",1000)
            }
        }
        save.setOnClickListener {
            if(dbManager.updateClient(Client(clientNameText,clientHostText,clientInnText,clientAddressText,clientIdText.toInt()),
                Client(name.text.toString(),host.text.toString(),inn.text.toString(), address.text.toString())
                )){
                MAIN.alert("${clientNameText} обновлено на ${name.text}",1000)
                MAIN.onClient()
            }
            else {
                MAIN.alert("Не получилось обновить ${clientNameText} на ${name.text}",1500)
                name.setText(clientNameText)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        dbManager.closeDb()
    }
}