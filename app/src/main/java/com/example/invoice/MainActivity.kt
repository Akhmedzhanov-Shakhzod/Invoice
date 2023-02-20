package com.example.invoice

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.invoice.databinding.ActivityMainBinding
import com.example.invoice.ui.helper.MAIN
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    private lateinit var pLauncher: ActivityResultLauncher<Array<String>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MAIN = this
        navController = Navigation.findNavController(this,R.id.nav_host_fragment_content_main)
        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_client, R.id.nav_product, R.id.nav_order
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        registerPermissionLauncher()
        checkREADEXTERNALSTORAGEPermission()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    ///////////////////////////////////////////////////////////////////////////////////////
    fun onClickAddProduct(view: View){
        navController.navigate(R.id.action_nav_product_to_nav_add_product)
    }
    fun onClickAddClient(view:View){
        navController.navigate(R.id.action_nav_client_to_addClientFragment)
    }
    fun onClickAddOrder(view:View){
        navController.navigate(R.id.action_nav_order_to_nav_home)
    }
    ///////////////////////////////////////////////////////////////////////////////////////
    fun onProduct(){
        navController.navigate(R.id.action_blankProductFragment_to_nav_product)
    }
    fun onClient(){
        navController.navigate(R.id.action_blankClientFragment_to_nav_client)
    }
    ///////////////////////////////////////////////////////////////////////////////////////
    fun alert(text:String, delay:Long = 500){
        val dialogBuilder = AlertDialog.Builder(this)
        // create dialog box
        val alert = dialogBuilder.create()
        alert.setMessage(text)
        alert.setOnShowListener { d ->
            Thread.sleep(delay)
            d.cancel()
        }
        // show alert dialog
        alert.show()
        //alert.cancel()
    }
    ///////////////////////////////////////////////////////////////////////////////////////
    private fun checkREADEXTERNALSTORAGEPermission(){
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED -> {
                    }
            else -> {
                pLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
            }
        }
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED -> {
                    }
            else -> {
                pLauncher.launch(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            }
        }
    }
    private fun registerPermissionLauncher(){
        pLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            if(it[Manifest.permission.READ_EXTERNAL_STORAGE] == true){
                Toast.makeText(this,"Доступ окрыть", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this,"Доступ отклонен", Toast.LENGTH_LONG).show()
            }
            if(it[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true){
                Toast.makeText(this,"Доступ окрыть", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this,"Доступ отклонен", Toast.LENGTH_LONG).show()
            }
        }
    }
}