package com.example.invoice.ui.order

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.invoice.R
import com.example.invoice.ui.helper.MAIN
import com.example.invoice.ui.helper.adapter.OrderProductsAdapter
import com.example.invoice.ui.helper.db.DbManager
import com.example.invoice.ui.helper.items.Client
import com.example.invoice.ui.helper.items.Order
import com.example.invoice.ui.helper.items.OrderProduct
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.apache.poi.xwpf.usermodel.*
import java.io.*
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class OrderProductsFragment : Fragment() {

    private val dbManager = DbManager(MAIN)
    private lateinit var client: Client
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_products, container, false)

        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbManager.openDb()

        val delete = view.findViewById<Button>(R.id.delete_order)
        val generateInvoice = view.findViewById<FloatingActionButton>(R.id.fab_generate_invoice)
        val list_view_order_products = view.findViewById<ListView>(R.id.list_view_order_products)

        val orderId = view.findViewById<TextView>(R.id.order_id)
        val clientName = view.findViewById<TextView>(R.id.order_client_name)
        val clientAddress = view.findViewById<TextView>(R.id.order_client_address)
        val orderAmount = view.findViewById<TextView>(R.id.order_amount)
        val orderDate = view.findViewById<TextView>(R.id.order_date)

        val order = Order(
            arguments?.getString("orderDate").toString(),
            arguments?.getString("clientId")!!.toInt(),
            arguments?.getString("orderAmount")!!.toDouble(),
            arguments?.getString("orderId")!!.toInt(),
        )

        val clients = dbManager.readFromClient
        while (clients.moveToNext()) {
            if(clients.getInt(0) == order.orderClientId){
                client = Client(clients.getString(1),clients.getString(2),clients.getString(3),
                    clients.getString(4),clients.getInt(0))
                clientName.text = clients.getString(1)
                clientAddress.text = clients.getString(4)
                break
            }
        }

        orderId.text = "№" + order.orderId.toString()
        orderAmount.text = order.amount.toString()
        orderDate.text = order.orderDate

        val products = dbManager.getOrderProducts(order.orderId!!)

        list_view_order_products.adapter = OrderProductsAdapter(MAIN,products)

        delete.setOnClickListener {
            if(dbManager.deleteOrders(order.orderId!!)){
                MAIN.alert("Заказ ${orderId.text} - удалено",1000)
                findNavController().navigate(R.id.nav_order)
            }
            else {
                MAIN.alert("Не получилось удалить заказ ${orderId.text}",1000)
            }
        }
        generateInvoice.setOnClickListener{
            generateWordFileFromTemplate("/sdcard/Download/template.docx",
                ("/sdcard/Download/Накладная (" + clientName.text.toString() + ")" + DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
                    .withZone(ZoneOffset.UTC)
                    .format(Instant.now())  + ".docx"),
                products,
                orderAmount.text.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        dbManager.closeDb()
    }

    private fun generateWordFileFromTemplate(templatePath: String, outputFilePath: String, products: ArrayList<OrderProduct>, amount: String) {

        try{
            val cal = Calendar.getInstance()
            val dayOfMonth = cal[Calendar.DAY_OF_MONTH]
            val month: String = cal.getDisplayName(
                Calendar.MONTH,
                Calendar.LONG_FORMAT, Locale("ru")
            ) as String
            val year = cal[Calendar.YEAR]

            if (ContextCompat.checkSelfPermission(MAIN, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // Разрешение на запись файлов уже есть, можно создавать файл
                val inputStream = FileInputStream(templatePath)
                val templateFile = XWPFDocument(inputStream)
                var isStarted = false
                for (element in templateFile.bodyElements) {
                    when (element.elementType) {
                        BodyElementType.TABLE -> {
                            val table = element as XWPFTable
                            var i = 0
                            for (row in table.rows) {
                                var j = 0
                                for (cell in row.tableCells) {
                                    for (paragraph in cell.paragraphs) {
                                        for (run in paragraph.runs) {
                                            val text = run.text()
                                            if(isStarted)
                                            {
                                                try{
                                                    val chek = products[i].orderId
                                                }
                                                catch (ex: Exception){
                                                    isStarted = false
                                                    break
                                                }
                                                if(j == 1) {
                                                    run.setText(text.replace(" ", products[i].product.name), 0)
                                                }
                                                else if(j == 2) {
                                                    run.setText(text.replace(" ", "кг"), 0)
                                                }
                                                else if(j == 3) {
                                                    run.setText(text.replace(" ", products[i].productCount.toString()), 0)
                                                }
                                                else if(j == 4) {
                                                    run.setText(text.replace(" ", products[i].product.price.toString()), 0)
                                                }
                                                else if(j == 6) {
                                                    run.setText(text.replace(" ", products[i].productAmount.toString()), 0)
                                                }
                                            }
                                            else if (text.contains("НаименованияМагазина")) {
                                                run.setText(text.replace("НаименованияМагазина", client.name), 0)
                                            }
                                            else if (text.contains("ФИО Владельца")) {
                                                run.setText(text.replace("ФИО Владельца", client.host), 0)
                                            }
                                            else if (text.contains("ИННН")) {
                                                run.setText(text.replace("ИННН", client.inn), 0)
                                            }
                                            else if (text.contains("День")) {
                                                run.setText(text.replace("День", dayOfMonth.toString()), 0)
                                            }
                                            else if (text.contains("Месяц")) {
                                                run.setText(text.replace("Месяц", month.toString()), 0)
                                            }
                                            else if (text.contains("Год")) {
                                                run.setText(text.replace("Год", year.toString()), 0)
                                            }
                                            else if (text.contains("Итого")) {
                                                run.setText(text.replace("Итого", amount), 0)
                                            }
                                            else if (text.contains("Начало")) {
                                                run.setText(text.replace("Начало", ""), 0)
                                                i = 0
                                                isStarted = true
                                            }
                                        }
                                    }
                                    j++
                                }
                                i++
                            }
                        }
                        else -> {
                        }
                    }
                }
                if (ContextCompat.checkSelfPermission(MAIN, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // Разрешение на запись файлов уже есть, можно создавать файл
                    val outFile = File(outputFilePath)
                    outFile.createNewFile()

                    val outputStream = FileOutputStream(outFile)
                    templateFile.write(outputStream)
                    templateFile.close()
                    MAIN.alert("Накладная генерировано в папку Download",1000)
                } else {
                    // Разрешение на запись файлов отсутствует, запрашиваем его у пользователя
                    ActivityCompat.requestPermissions(MAIN, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 200)
                }
            } else {
                // Разрешение на запись файлов отсутствует, запрашиваем его у пользователя
                ActivityCompat.requestPermissions(MAIN, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 200)
            }
        }
        catch (ex: Exception){
            ex.printStackTrace()
            MAIN.alert("Не получилось сгенирировать накладную, пожалуйста обращайтесь Ахмеджанову Шахзоду)",2500)
        }
    }
}