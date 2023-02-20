package com.example.invoice.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.invoice.R
import com.example.invoice.databinding.FragmentProductBinding
import com.example.invoice.ui.helper.MAIN
import com.example.invoice.ui.helper.adapter.ProductAdapter
import com.example.invoice.ui.helper.db.DbManager
import com.example.invoice.ui.helper.items.Product

class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private val products_arrayList:ArrayList<Product> = ArrayList()
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

        _binding = FragmentProductBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dbManager.openDb()
        val product = dbManager.readFromProduct

        while (product.moveToNext()) {
            products_arrayList.add(Product(product.getString(1),product.getString(2).toDouble(),product.getInt(0)))
        }

        binding.listViewProduct.isClickable = true
        binding.listViewProduct.adapter = ProductAdapter(MAIN,products_arrayList)
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = Bundle()

        binding.listViewProduct.setOnItemClickListener { parent, view, position, id ->

            val productId:Int? = products_arrayList[position].productId
            val name:String = products_arrayList[position].name
            val price:Double = products_arrayList[position].price

            bundle.putString("product_id", productId.toString())
            bundle.putString("product_name", name)
            bundle.putString("product_price", price.toString())

            findNavController().navigate(R.id.blankProductFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        dbManager.closeDb()
        products_arrayList.clear()
    }
}