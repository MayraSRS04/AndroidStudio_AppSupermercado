package com.example.proyectofinalprograiii

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ReporteVentasActivity : AppCompatActivity() {

    private lateinit var conexionSQL: ConexionSQL
    private lateinit var spinnerReportes: Spinner
    private lateinit var listViewReporte: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte_ventas)

        conexionSQL = ConexionSQL(this)
        spinnerReportes = findViewById(R.id.spReportes)
        listViewReporte = findViewById(R.id.lvReportes)
        val tvCerrarRVentas: TextView = findViewById(R.id.tvCerrarReporteVentas)
        tvCerrarRVentas.setOnClickListener{ finish()}

        spReportes()
    }

    private fun spReportes() {
        val opcionesReporte = arrayOf("Vendedor", "Cliente", "Categoría")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesReporte)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerReportes.adapter = adapter

        spinnerReportes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when (position) {
                    0 -> mostrarReporteVendedores()
                    1 -> mostrarReporteClientes()
                    2 -> mostrarReporteCategorias()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun mostrarReporteVendedores() {
        val listaVendedores = obtenerVendedores()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaVendedores)
        listViewReporte.adapter = adapter

        listViewReporte.setOnItemClickListener { _, _, position, _ ->
            val vendedorSeleccionado = listaVendedores[position]
            mostrarDetalleVentasVendedor(vendedorSeleccionado)
        }
    }

    private fun obtenerVendedores(): List<String> {
        val listaVendedores = mutableListOf<String>()
        val db = conexionSQL.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Vendedor", null)

        while (cursor.moveToNext()) {
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombreCompleto"))
            listaVendedores.add(nombre)
        }
        cursor.close()
        return listaVendedores
    }

    private fun mostrarDetalleVentasVendedor(nombreVendedor: String) {
        val db = conexionSQL.readableDatabase
        val cursor = db.rawQuery("SELECT Venta.numFactura, Venta.fecha, Cliente.nombreCompleto FROM Venta INNER JOIN Cliente ON Venta.clienteId = Cliente.id INNER JOIN Vendedor ON Venta.vendedorId = Vendedor.id WHERE Vendedor.nombreCompleto = ?", arrayOf(nombreVendedor))

        val detallesVentas = mutableListOf<String>()
        while (cursor.moveToNext()) {
            val numFactura = cursor.getString(cursor.getColumnIndexOrThrow("numFactura"))
            val fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"))
            val nombreCliente = cursor.getString(cursor.getColumnIndexOrThrow("nombreCompleto"))

            detallesVentas.add("Factura: $numFactura, Fecha: $fecha, Cliente: $nombreCliente")
        }
        cursor.close()

        val intent = Intent(this, DetalleVentasVendedorActivity::class.java)
        intent.putStringArrayListExtra("detallesVentas", ArrayList(detallesVentas))
        startActivity(intent)
    }


    private fun mostrarReporteClientes() {
        val listaClientes = obtenerClientes()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaClientes)
        listViewReporte.adapter = adapter
        listViewReporte.setOnItemClickListener { _, _, position, _ ->
            val clienteSeleccionado = listaClientes[position]
            mostrarDetalleComprasCliente(clienteSeleccionado)
        }
    }

    private fun obtenerClientes(): List<String> {
        val clientes = mutableListOf<String>()
        val db = conexionSQL.readableDatabase
        val cursor = db.query("Cliente", arrayOf("nombreCompleto"), null, null, null, null, null)

        while (cursor.moveToNext()) {
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombreCompleto"))
            clientes.add(nombre)
        }
        cursor.close()
        db.close()
        return clientes
    }

    private fun mostrarDetalleComprasCliente(cliente: String) {
        val db = conexionSQL.readableDatabase
        val cursor = db.rawQuery(
            "SELECT COUNT(*), SUM(precioUnitario * cantidad) FROM Venta INNER JOIN DetalleVenta ON Venta.id = DetalleVenta.ventaId WHERE clienteId = (SELECT id FROM Cliente WHERE nombreCompleto = ?)",
            arrayOf(cliente)
        )

        if (cursor.moveToFirst()) {
            val numeroCompras = cursor.getInt(0)
            val totalGastado = cursor.getDouble(1)
            cursor.close()

            AlertDialog.Builder(this)
                .setTitle("Detalle de Compras")
                .setMessage("Cliente: $cliente\nNúmero de Compras: $numeroCompras\nTotal Gastado: $totalGastado")
                .setPositiveButton("OK", null)
                .show()
        }
        db.close()
    }


    private fun mostrarReporteCategorias() {
        val listaCategorias = obtenerCategorias()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaCategorias)
        listViewReporte.adapter = adapter
        listViewReporte.setOnItemClickListener { _, _, position, _ ->
            val categoriaSeleccionada = listaCategorias[position]
            mostrarDetalleVentasCategoria(categoriaSeleccionada)
        }
    }

    private fun obtenerCategorias(): List<String> {
        val categorias = mutableListOf<String>()
        val db = conexionSQL.readableDatabase
        val cursor = db.rawQuery("SELECT nombre FROM Categoria", null)
        while (cursor.moveToNext()) {
            categorias.add(cursor.getString(0))
        }
        cursor.close()
        db.close()
        return categorias
    }

    private fun mostrarDetalleVentasCategoria(categoria: String) {
        val db = conexionSQL.readableDatabase
        val cursor = db.rawQuery(
            "SELECT SUM(DetalleVenta.cantidad) FROM DetalleVenta INNER JOIN Producto ON DetalleVenta.productoId = Producto.id WHERE Producto.categoriaId = (SELECT id FROM Categoria WHERE nombre = ?)",
            arrayOf(categoria)
        )

        if (cursor.moveToFirst()) {
            val totalProductosVendidos = cursor.getInt(0)
            cursor.close()

            AlertDialog.Builder(this)
                .setTitle("Detalle de Ventas por Categoría")
                .setMessage("Categoría: $categoria\nTotal de Productos Vendidos: $totalProductosVendidos")
                .setPositiveButton("OK", null)
                .show()
        }
        db.close()
    }
}
