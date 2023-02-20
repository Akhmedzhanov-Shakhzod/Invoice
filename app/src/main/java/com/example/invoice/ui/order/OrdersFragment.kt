package com.example.invoice.ui.order

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.invoice.R
import com.example.invoice.databinding.FragmentOrderBinding
import com.example.invoice.ui.helper.MAIN
import com.example.invoice.ui.helper.adapter.OrderAdapter
import com.example.invoice.ui.helper.db.DbManager
import com.example.invoice.ui.helper.items.Order
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrdersFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null

    private val dbManager = DbManager(MAIN)
    private val formatter = SimpleDateFormat("dd/MM/yyyy")
    private var startDate: Date? = null
    private var endDate: Date? = null

    private val orders_arraylist = ArrayList<Order>()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textStartDate = binding.textStartDate
        val textEndDate = binding.textEndDate
        val buttonStartDate = binding.buttonStartDate
        val buttonEndDate = binding.buttonEndDate

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
//        var hour = calendar.get(Calendar.HOUR_OF_DAY)
//        val minute = calendar.get(Calendar.MINUTE)

        startDate = formatter.parse("$day/${month + 1}/$year") as Date
        endDate = formatter.parse("$day/${month + 1}/$year") as Date
        textStartDate?.text = "$day/${month + 1}/$year"
        textEndDate?.text = "$day/${month + 1}/$year"

        buttonStartDate.setOnClickListener {
            val dpd = DatePickerDialog(
                MAIN,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    textStartDate.text = "$mDay/${mMonth + 1}/$mYear"
                    startDate = formatter.parse(textStartDate.text.toString()) as Date
                    orders_arraylist.clear()
                    onViewCreated(root,null)
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        buttonEndDate.setOnClickListener {
            val dpd = DatePickerDialog(
                MAIN,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    textEndDate.text = "$mDay/${mMonth + 1}/$mYear"
                    endDate = formatter.parse(textEndDate.text.toString()) as Date
                    orders_arraylist.clear()
                    onViewCreated(root,null)
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbManager.openDb()

        val listViewOrders = binding.listViewOrders
        val clearOrders = binding.clearOrders

        val orders = dbManager.readFromOrders

        while (orders.moveToNext()) {
            val orderDate = formatter.parse(orders.getString(1)) as Date
            if(startDate == null && endDate == null) {
                orders_arraylist.add(Order(orders.getString(1),orders.getInt(2),
                    orders.getDouble(3),orders.getInt(0)))
            }
            else if(startDate != null && endDate != null){
                if(orderDate >= startDate && orderDate <= endDate){
                    orders_arraylist.add(Order(orders.getString(1),orders.getInt(2),
                        orders.getDouble(3),orders.getInt(0)))
                }
            }
            else if (startDate != null){
                if(orderDate >= startDate){
                    orders_arraylist.add(Order(orders.getString(1),orders.getInt(2),
                        orders.getDouble(3),orders.getInt(0)))
                }
            }
            else if (endDate != null){
                if(orderDate <= endDate) {
                    orders_arraylist.add(Order(orders.getString(1),orders.getInt(2),
                        orders.getDouble(3),orders.getInt(0)))
                }
            }
        }


        listViewOrders.isClickable = true
        listViewOrders.adapter = OrderAdapter(MAIN,orders_arraylist)

        listViewOrders.setOnItemClickListener { parent, view, position, id ->
            listViewOrders.getItemAtPosition(position)

            val bundle = Bundle()

            orders_arraylist[position].orderId?.let { bundle.putInt("orderId", it) }
            bundle.putString("orderId",orders_arraylist[position].orderId.toString())
            bundle.putString("clientId",orders_arraylist[position].orderClientId.toString())
            bundle.putString("orderAmount", orders_arraylist[position].amount.toString())
            bundle.putString("orderDate",orders_arraylist[position].orderDate)

            findNavController().navigate(R.id.orderProductsFragment,bundle)
        }


        clearOrders.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(MAIN)
            // create dialog box
            dialogBuilder.setMessage("Уверены что хотите удалить все заказы ?!")
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton("Да", DialogInterface.OnClickListener {
                        dialog, id ->
                    if(dbManager.clearOrders() && dbManager.clearOrderProducts()) {
                        listViewOrders.adapter = null
                        orders_arraylist.clear()
                    }
                    else {
                        MAIN.alert("Что-то пошло не так :(")
                        dialog.cancel()
                    }
                })
                // negative button text and action
                .setNegativeButton("Отменить", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })
            val alert = dialogBuilder.create()
            // show alert dialog
            alert.show()
            //alert.cancel()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        orders_arraylist.clear()
        dbManager.closeDb()
    }
}