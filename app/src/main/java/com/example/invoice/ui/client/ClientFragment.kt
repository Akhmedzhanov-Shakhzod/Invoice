package com.example.invoice.ui.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.invoice.R
import com.example.invoice.databinding.FragmentClientBinding
import com.example.invoice.ui.helper.MAIN
import com.example.invoice.ui.helper.adapter.ClientAdapter
import com.example.invoice.ui.helper.db.DbManager
import com.example.invoice.ui.helper.items.Client

class ClientFragment : Fragment() {

    private var _binding: FragmentClientBinding? = null
    private val client_arrayList:ArrayList<Client> = ArrayList()
    private val dbManager = DbManager(MAIN)
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(ClientViewModel::class.java)

        _binding = FragmentClientBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dbManager.openDb()
        val clients = dbManager.readFromClient

        while (clients.moveToNext()) {
            client_arrayList.add(Client(clients.getString(1),clients.getString(2),
                clients.getString(3),clients.getString(4),clients.getInt(0)))
        }
        binding.listViewClient.isClickable = true
        binding.listViewClient.adapter = ClientAdapter(MAIN,client_arrayList)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = Bundle()

        binding.listViewClient.setOnItemClickListener { parent, view, position, id ->

            val clientId:Int? = client_arrayList[position].clientId
            val name:String = client_arrayList[position].name
            val host:String = client_arrayList[position].host
            val inn:String = client_arrayList[position].inn
            val address:String = client_arrayList[position].address

            bundle.putString("client_id", clientId.toString())
            bundle.putString("client_name", name)
            bundle.putString("client_host", host)
            bundle.putString("client_inn", inn)
            bundle.putString("client_address", address)

            findNavController().navigate(R.id.blankClientFragment, bundle)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        dbManager.closeDb()
        client_arrayList.clear()
    }
}