package com.example.invoice.ui.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.invoice.databinding.FragmentAddClientBinding
import com.example.invoice.ui.helper.MAIN
import com.example.invoice.ui.helper.db.DbManager
import com.example.invoice.ui.helper.items.Client

class AddClientFragment : Fragment() {

    private var _binding: FragmentAddClientBinding? = null

    private val dbManager = DbManager(MAIN)

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentAddClientBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dbManager.openDb()

        binding.addNameClient.requestFocus()
        binding.addClient.setOnClickListener {
            val clientNameText = binding.addNameClient.text.toString()
            val clientHostText = binding.addHostClient.text.toString()
            val clientInnText = binding.addInnClient.text.toString()
            val clientAddressText = binding.addAddressClient.text.toString()
            val name = clientNameText.trim(' ')
            val host = clientHostText.trim(' ')
            val inn = clientInnText.trim(' ')
            val address = clientAddressText.trim(' ')
            if(name.isNotEmpty() && inn.isNotEmpty()) {
                val result = dbManager.insertClientToDb(Client(name,host,inn,address))

                if (result == 0) {
                    MAIN.alert("${name} - добавлено")
                    binding.addNameClient.setText("")
                    binding.addHostClient.setText("")
                    binding.addInnClient.setText("")
                    binding.addAddressClient.setText("")
                }
                else if (result == 1) {
                    MAIN.alert("${name} - уже существует :)", 1000)
                }
                else if (result == 2) {
                    MAIN.alert("Что-то пошло не так :(", 1000)
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        dbManager.closeDb()
    }
}