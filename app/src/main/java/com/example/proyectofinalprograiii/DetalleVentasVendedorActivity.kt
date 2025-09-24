package com.example.proyectofinalprograiii

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetalleVentasVendedorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_ventas_vendedor)

        val listViewDetalleVentas = findViewById<ListView>(R.id.lvDetalleVentas)
        val tvCerrarDetVentaVendedor: TextView = findViewById(R.id.tvDetVentaVendedor)
        tvCerrarDetVentaVendedor.setOnClickListener{ finish()}

        val detallesVentas = intent.getStringArrayListExtra("detallesVentas")

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, detallesVentas ?: listOf())
        listViewDetalleVentas.adapter = adapter
    }
}
