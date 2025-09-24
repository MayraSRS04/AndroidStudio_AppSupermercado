package com.example.proyectofinalprograiii

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: ConexionSQL
    private lateinit var database: SQLiteDatabase
    private lateinit var exportarExcel: ExportarExcel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        databaseHelper = ConexionSQL(this)
        database = databaseHelper.writableDatabase

        // Inicializar ExportarExcel y configurar el botón de exportación
        exportarExcel = ExportarExcel(this, database)

        val btnExportarDatos: Button = findViewById(R.id.btnExportarDatos)
        btnExportarDatos.setOnClickListener {
            exportarExcel.exportarProductosACategoria()
        }

        val btLogin: Button = findViewById(R.id.btLogin)
        val btRegisterProduct: Button = findViewById(R.id.btRegistrarProducto)
        btRegisterProduct.isEnabled= false
        val btRegisterClient: Button = findViewById(R.id.btRegistrarCliente)
        btRegisterClient.isEnabled= false
        val btSalesReport: Button = findViewById(R.id.btReporteVentas)
        btSalesReport.isEnabled= false
        val btRegisVenta: Button= findViewById(R.id.btRegistrarVenta)
        btRegisVenta.isEnabled= false
        val btIniSesion: Button= findViewById(R.id.btInicioSesion)

        val sharedPref = getSharedPreferences("App", Context.MODE_PRIVATE)
        val sesionIniciada = sharedPref.getBoolean("SesionIni", false)

        if (sesionIniciada) {
            btRegisterProduct.isEnabled = true
            btRegisterClient.isEnabled = true
            btSalesReport.isEnabled = true
            btRegisVenta.isEnabled = true
        }

        btRegisVenta.setOnClickListener {
            val intent = Intent(this, RegistrarVentaActivity::class.java)
            startActivity(intent)
        }

        btIniSesion.setOnClickListener {

            val intent = Intent(this, IniciarSesionActivity::class.java)
            startActivity(intent)

        }

        btLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btRegisterProduct.setOnClickListener {
            val intent = Intent(this, RegistrarProductoActivity::class.java)
            startActivity(intent)
        }

        btRegisterClient.setOnClickListener {
            val intent = Intent(this, RegistrarClienteActivity::class.java)
            startActivity(intent)
        }

        btSalesReport.setOnClickListener {
            val intent = Intent(this, ReporteVentasActivity::class.java)
            startActivity(intent)
        }


    }
}
