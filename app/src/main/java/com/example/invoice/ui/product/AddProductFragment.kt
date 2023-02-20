package com.example.invoice.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.invoice.databinding.FragmentAddProductBinding
import com.example.invoice.ui.helper.MAIN
import com.example.invoice.ui.helper.db.DbManager
import com.example.invoice.ui.helper.items.Product

class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val dbManager = DbManager(MAIN)
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(ProductViewModel::class.java)

        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dbManager.openDb()

        binding.editNameProduct.requestFocus()
        binding.addPoduct.setOnClickListener {
            val productNameText = binding.editNameProduct.text.toString()
            val productPriceText = binding.editPriceProduct.text.toString()

            val name = productNameText.trim(' ')
            val price = productPriceText.trim(' ')
            if(name.isNotEmpty() && price.isNotEmpty()) {
                val product = Product(name,price.toDouble())
                val iresult = dbManager.insertProductToDb(product)

                if (iresult == 0) {
                    MAIN.alert("${name} - добавлено")
                    binding.editNameProduct.setText("")
                    binding.editPriceProduct.setText("")
                }
                else if (iresult == 1) {
                    MAIN.alert("${name} - уже существует :)", 1000)
                }
                else if (iresult == 2) {
                    MAIN.alert("Что-то пошло не так :(", 1000)
                }
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}