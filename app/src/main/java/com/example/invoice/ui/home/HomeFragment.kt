package com.example.invoice.ui.home

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.invoice.R
import com.example.invoice.databinding.FragmentHomeBinding
import com.example.invoice.ui.helper.CHOSENPRODUCTSDATAMODEL
import com.example.invoice.ui.helper.MAIN
import com.example.invoice.ui.helper.adapter.ChoseProductAdapter
import com.example.invoice.ui.helper.db.DbManager
import com.example.invoice.ui.helper.items.Order
import com.example.invoice.ui.helper.items.OrderProduct
import com.example.invoice.ui.helper.items.Product

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textData = binding.textDate
        val buttonDate = binding.buttonDate

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
//        var hour = calendar.get(Calendar.HOUR_OF_DAY)
//        val minute = calendar.get(Calendar.MINUTE)

        textData.text = "$day/${month+1}/$year"

        buttonDate.setOnClickListener {
            val dpd = DatePickerDialog(MAIN, DatePickerDialog.OnDateSetListener{
                    view,mYear,mMonth,mDay ->
                textData.text = "$mDay/${mMonth+1}/$mYear"
            },year,month,day)
            dpd.show()
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var amount:Double = 0.00

        val amoutText = binding.amountText
        val choseShop = binding.choseShop
        val choseProduct = binding.choseProduct
        val addOrder = binding.addOrderButton
        val clear = binding.clear

        val clientNameText = CHOSENPRODUCTSDATAMODEL.clientName
        choseShop.text = clientNameText

        binding.listChosenViewProduct.isClickable = true

        if(CHOSENPRODUCTSDATAMODEL.product.isNotEmpty()){
            binding.listChosenViewProduct.adapter = ChoseProductAdapter(MAIN,CHOSENPRODUCTSDATAMODEL.product)
            for (i in 0 until CHOSENPRODUCTSDATAMODEL.product.count()){
                amount += CHOSENPRODUCTSDATAMODEL.productAmount[i]
            }
        }
        binding.listChosenViewProduct.setOnItemClickListener { parent, view, position, id ->
            val bundle = Bundle()

            val name:String = CHOSENPRODUCTSDATAMODEL.product[position].first.name
            val price:Double = CHOSENPRODUCTSDATAMODEL.product[position].first.price
            val count:Double = CHOSENPRODUCTSDATAMODEL.product[position].second

            bundle.putString("product_name",name)
            bundle.putString("product_price",price.toString())
            bundle.putString("product_count",count.toString())

            findNavController().navigate(R.id.productFromAddOrderBlankFragment,bundle)
        }

        amoutText.text = amount.toString()
        choseShop.setOnClickListener {
            findNavController().navigate(R.id.choseClientFragment)
        }

        choseProduct.setOnClickListener {
            findNavController().navigate(R.id.choseProductsFragment)
        }
        addOrder.setOnClickListener {
            if(choseShop.text.isNotEmpty() && CHOSENPRODUCTSDATAMODEL.product.isNotEmpty()){
                val dbManager = DbManager(MAIN)
                dbManager.openDb()

                val order = Order(binding.textDate.text.toString(),
                    CHOSENPRODUCTSDATAMODEL.clientId!!,binding.amountText.text.toString().toDouble())
                var iresult1 = dbManager.insertOrderToDb(order)
                var iresult2 = 0

                val orderId = dbManager.getOrderId(order)
                if(orderId == -1){
                    iresult1 = 2
                }

                for(i in 0 until CHOSENPRODUCTSDATAMODEL.product.count()){
                    val productId = dbManager.getProductId(CHOSENPRODUCTSDATAMODEL.product[i].first)
                    if(productId == -1) {
                        iresult1 = 2
                    }
                    val op = OrderProduct(orderId, Product(CHOSENPRODUCTSDATAMODEL.product[i].first.name,
                        CHOSENPRODUCTSDATAMODEL.product[i].first.price), CHOSENPRODUCTSDATAMODEL.product[i].second)
                    iresult2 = dbManager.insertOrderProductToDb(op)
                }

                if (iresult1 == 0 && iresult2 == 0) {
                    MAIN.alert("Заказ №$orderId добавлен",1000)
                    CHOSENPRODUCTSDATAMODEL.clientName = ""
                    CHOSENPRODUCTSDATAMODEL.product.clear()
                    CHOSENPRODUCTSDATAMODEL.productAmount.clear()
                }
                else if (iresult1 == 1 && iresult2 == 1) {
                    MAIN.alert("этот заказ - уже существует :)", 1000)
                }
                else {
                    MAIN.alert("Что-то пошло не так :(", 1000)
                }

                dbManager.closeDb()

                findNavController().navigate(R.id.nav_home)
            }
            else if (choseShop.text.isEmpty()){
                MAIN.alert("Выберите магазин :) ",1000)
            }
            else {
                MAIN.alert("Выберите хотя бы один продукт :) ",1000)
            }
        }

        clear.setOnClickListener {
            CHOSENPRODUCTSDATAMODEL.clientName = ""
            CHOSENPRODUCTSDATAMODEL.product.clear()
            CHOSENPRODUCTSDATAMODEL.productAmount.clear()

            findNavController().navigate(R.id.nav_home)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}