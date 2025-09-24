package com.example.proyectofinalprograiii


import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegistrarVentaActivity : AppCompatActivity() {
    private lateinit var conexionSQL: ConexionSQL
    private lateinit var rgMetodoPago: RadioGroup
    private lateinit var rbEfectivo: RadioButton
    private lateinit var rbTarjeta: RadioButton
    private lateinit var rbQR: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_venta)

        conexionSQL = ConexionSQL(this)

        val spinnerProductos: Spinner = findViewById(R.id.spProductos)
        val spinnerClientes: Spinner = findViewById(R.id.spClientes)
        val etCantidad: EditText = findViewById(R.id.etCantidad)
        val etNumFactura: EditText = findViewById(R.id.etNumFactura)
        val tvResumenVenta: TextView = findViewById(R.id.tvResumenVenta)
        val btGuardarVenta: Button = findViewById(R.id.btGuardarVenta)
        val tvCerrarRVenta: TextView = findViewById(R.id.tvCerrarRegVenta)
        tvCerrarRVenta.setOnClickListener{ finish()}

        rgMetodoPago = findViewById(R.id.rgMetodoPago)
        rbEfectivo = findViewById(R.id.rbEfectivo)
        rbEfectivo.isChecked= true
        rbTarjeta = findViewById(R.id.rbTarjeta)
        rbQR = findViewById(R.id.rbQR)

        cargarProductos()
        cargarClientes()

        btGuardarVenta.setOnClickListener {

            val producto = spinnerProductos.selectedItem as Producto
            val cliente = spinnerClientes.selectedItem as Cliente
            val cantidad = etCantidad.text.toString().toIntOrNull() ?: 0
            val numFactura = etNumFactura.text.toString()
            val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val metodoPagoSeleccionado = when (rgMetodoPago.checkedRadioButtonId) {
                R.id.rbEfectivo -> "Efectivo"
                R.id.rbTarjeta -> "Tarjeta"
                R.id.rbQR -> "QR"
                else -> ""
            }

            if (cantidad > 0 && numFactura.isNotEmpty() && metodoPagoSeleccionado.isNotEmpty() ) {
                val db = conexionSQL.writableDatabase
                val valuesVenta = ContentValues().apply {
                    put("vendedorId", 1)
                    put("clienteId", cliente.id)
                    put("fecha", fecha)
                    put("numFactura", numFactura)
                }

                val ventaId = db.insert("Venta", null, valuesVenta)
                if (ventaId != -1L) {
                    val valuesDetalleVenta = ContentValues().apply {
                        put("ventaId", ventaId)
                        put("productoId", producto.id)
                        put("cantidad", cantidad)
                        put("precioUnitario", producto.precioUnitario)
                    }
                    db.insert("DetalleVenta", null, valuesDetalleVenta)

                    // resumen de la venta
                    val resumen = """
                        Factura N°: $numFactura
                        Producto: ${producto.nombre}
                        Cantidad: $cantidad
                        Precio Unitario: ${producto.precioUnitario}
                        Total: ${producto.precioUnitario * cantidad}
                        Método de pago: $metodoPagoSeleccionado
                    """.trimIndent()
                    //eliminar espacios en blanco
                    tvResumenVenta.text = resumen

                    Toast.makeText(this, "Venta registrada con éxito", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al registrar la venta", Toast.LENGTH_SHORT).show()
                }

                db.close()
            } else {
                Toast.makeText(
                    this,
                    "Debe completar todos los campos correctamente",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun cargarProductos() {
        val db = conexionSQL.readableDatabase
        val cursor = db.query("Producto", arrayOf("id", "nombre", "precioUnitario"), null, null, null, null, null)
        val productos = arrayListOf<Producto>()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            val precioUnitario = cursor.getDouble(cursor.getColumnIndexOrThrow("precioUnitario"))
            productos.add(Producto(id, nombre, precioUnitario))
        }
        cursor.close()

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, productos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinnerProductos: Spinner = findViewById(R.id.spProductos)
        spinnerProductos.adapter = adapter
    }

    private fun cargarClientes() {
        val db = conexionSQL.readableDatabase
        val cursor = db.query("Cliente", arrayOf("id", "nombreCompleto"), null, null, null, null, null)
        val clientes = arrayListOf<Cliente>()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val nombreCompleto = cursor.getString(cursor.getColumnIndexOrThrow("nombreCompleto"))
            clientes.add(Cliente(id, nombreCompleto))
        }
        cursor.close()

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, clientes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinnerClientes: Spinner = findViewById(R.id.spClientes)
        spinnerClientes.adapter = adapter
    }

    // Clases Producto y Cliente que se necesita para los spinners
    data class Producto(val id: Int, val nombre: String, val precioUnitario: Double) {
        override fun toString(): String {
            return nombre
        }
    }

    data class Cliente(val id: Int, val nombreCompleto: String) {
        override fun toString(): String {
            return nombreCompleto
        }
    }
}

