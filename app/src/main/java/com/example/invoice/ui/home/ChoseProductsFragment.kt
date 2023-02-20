package com.example.invoice.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.invoice.R
import com.example.invoice.databinding.FragmentAddOrderChoseProductsBinding
import com.example.invoice.ui.helper.MAIN
import com.example.invoice.ui.helper.adapter.ProductAdapter
import com.example.invoice.ui.helper.db.DbManager
import com.example.invoice.ui.helper.items.Product

class ChoseProductsFragment : Fragment() {

    private var _binding: FragmentAddOrderChoseProductsBinding? = null

    private val products_arrayList:ArrayList<Product> = ArrayList()
    private val dbManager = DbManager(MAIN)
    private var searchText:String? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddOrderChoseProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val searchProduct = binding.searchProduct

        searchProduct.addTextChangedListener {
            searchText = searchProduct.text.toString()
            binding.listViewChoseProduct.adapter = null
            products_arrayList.clear()
            onViewCreated(root,null)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbManager.openDb()
        val product = dbManager.readFromProduct

        binding.listViewChoseProduct.isClickable = true

        while (product.moveToNext()) {
            products_arrayList.add(Product(product.getString(1),product.getString(2).toDouble()))
        }

        if(searchText != null) {
            val tempArrayList = products_arrayList.filter {
                it.name.uppercase().contains(searchText.toString().uppercase())
            }
            binding.listViewChoseProduct.adapter = ProductAdapter(MAIN,
                tempArrayList as ArrayList<Product>
            )
            val bundle = Bundle()
            binding.listViewChoseProduct.setOnItemClickListener { parent, view, position, id ->

                val name:String = tempArrayList[position].name
                val price:Double = tempArrayList[position].price

                bundle.putString("product_name",name)
                bundle.putString("product_price",price.toString())

                findNavController().navigate(R.id.productCountBlankFragment,bundle)
            }
        }
        else {
            binding.listViewChoseProduct.adapter = ProductAdapter(MAIN,products_arrayList)
            val bundle = Bundle()
            binding.listViewChoseProduct.setOnItemClickListener { parent, view, position, id ->

                val name:String = products_arrayList[position].name
                val price:Double = products_arrayList[position].price

                bundle.putString("product_name",name)
                bundle.putString("product_price",price.toString())

                findNavController().navigate(R.id.productCountBlankFragment,bundle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        dbManager.closeDb()
        products_arrayList.clear()
    }
}