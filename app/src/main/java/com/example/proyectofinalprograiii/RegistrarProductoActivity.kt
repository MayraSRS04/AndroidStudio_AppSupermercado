package com.example.proyectofinalprograiii

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

class RegistrarProductoActivity : AppCompatActivity() {

    private lateinit var conexionSQL: ConexionSQL
    private lateinit var spinnerCategorias: Spinner
    private lateinit var spinnerProductos: Spinner
    private lateinit var etNombre: EditText
    private lateinit var etMarca: EditText
    private lateinit var etPrecio: EditText
    private lateinit var btGuardarProducto: Button
    private lateinit var btActualizarProducto: Button
    private lateinit var btEliminarProducto: Button
    private lateinit var productos: ArrayList<Producto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_producto)

        conexionSQL = ConexionSQL(this)
        spinnerCategorias = findViewById(R.id.sp)
        spinnerProductos = findViewById(R.id.spProductosss)
        etNombre = findViewById(R.id.etNombreP)
        etMarca = findViewById(R.id.etMarcaP)
        etPrecio = findViewById(R.id.etPrecioUniProducto)
        btGuardarProducto = findViewById(R.id.btGuardarProducto)
        btActualizarProducto = findViewById(R.id.btnActualizarPro)
        btEliminarProducto = findViewById(R.id.btnEliminarPro)
        val tvCerrarRProducto: TextView = findViewById(R.id.tvCerrarRProducto)
        tvCerrarRProducto.setOnClickListener{ finish()}

        cargarCategorias()
        cargarProductos()

        spinnerProductos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val producto = productos[position]
                etNombre.setText(producto.nombre)
                etMarca.setText(producto.marca)
                etPrecio.setText(producto.precioUnitario.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        btGuardarProducto.setOnClickListener {
            val nombre = etNombre.text.toString()
            val marca = etMarca.text.toString()
            val precio = etPrecio.text.toString().toDoubleOrNull() ?: 0.0
            val categoriaId = (spinnerCategorias.selectedItem as Categoria).id

            if (nombre.isNotEmpty() && marca.isNotEmpty() && precio > 0 ) {
                val db = conexionSQL.writableDatabase
                val values = ContentValues().apply {
                    put("nombre", nombre)
                    put("marca", marca)
                    put("precioUnitario", precio)
                    put("categoriaId", categoriaId)
                }
                //verifica que el iniciar sesion sea correcta
                //insert devuelve long es el id de la fila recien insertada o -1 si falla
                val newRowId = db.insert("Producto", null, values)
                if (newRowId != -1L) {
                    Toast.makeText(this, "Producto registrado con éxito", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al registrar el producto", Toast.LENGTH_SHORT).show()
                }

                db.close()
            } else {
                Toast.makeText(this, "Debe completar todos los campos correctamente", Toast.LENGTH_SHORT).show()
            }
        }

        btActualizarProducto.setOnClickListener {
            val productoSeleccionado = spinnerProductos.selectedItem as Producto
            actualizarProducto(productoSeleccionado.id)
            etNombre.text.clear()
            etMarca.text.clear()
            etPrecio.text.clear()
        }

        btEliminarProducto.setOnClickListener {
            val productoSeleccionado = spinnerProductos.selectedItem as Producto
            eliminarProducto(productoSeleccionado.id)
            etNombre.text.clear()
            etMarca.text.clear()
            etPrecio.text.clear()
        }
    }

    private fun cargarCategorias() {
        val db = conexionSQL.readableDatabase
        val cursor = db.query("Categoria", arrayOf("id", "nombre"), null, null, null, null, null)
        val categorias = arrayListOf<Categoria>()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            categorias.add(Categoria(id, nombre))
        }
        cursor.close()

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategorias.adapter = adapter

    }

    private fun cargarProductos() {
        productos = arrayListOf()
        val db = conexionSQL.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Producto", null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            val marca = cursor.getString(cursor.getColumnIndexOrThrow("marca"))
            val precioUnitario = cursor.getDouble(cursor.getColumnIndexOrThrow("precioUnitario"))
            productos.add(Producto(id, nombre, marca, precioUnitario))
        }
        cursor.close()

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, productos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProductos.adapter = adapter
    }

    private fun actualizarProducto(id: Int) {
        val nombre = etNombre.text.toString()
        val marca = etMarca.text.toString()
        val precioUnitario = etPrecio.text.toString().toDoubleOrNull() ?: 0.0
        val categoriaId = (spinnerCategorias.selectedItem as Categoria).id

        val db = conexionSQL.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", nombre)
            put("marca", marca)
            put("precioUnitario", precioUnitario)
            put("categoriaId", categoriaId)
        }

        val cantidad = db.update("Producto", valores, "id=?", arrayOf(id.toString()))
        if (cantidad > 0) {
            Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error al actualizar el producto", Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    private fun eliminarProducto(id: Int) {
        val db = conexionSQL.writableDatabase
        val cantidad = db.delete("Producto", "id=?", arrayOf(id.toString()))
        if (cantidad > 0) {
            Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error al eliminar el producto", Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    //contenedor de datos
    data class Producto(val id: Int, val nombre: String, val marca: String, val precioUnitario: Double) {
        override fun toString(): String {
            return nombre
        }
    }
    data class Categoria(val id: Int, val nombre: String) {
        override fun toString(): String {
            return nombre
        }
    }
}
