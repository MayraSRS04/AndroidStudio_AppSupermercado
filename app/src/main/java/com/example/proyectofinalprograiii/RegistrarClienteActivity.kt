package com.example.proyectofinalprograiii

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegistrarClienteActivity : AppCompatActivity() {

    private lateinit var conexionSQL: ConexionSQL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_cliente)

        conexionSQL = ConexionSQL(this)

        val etCarnet: EditText = findViewById(R.id.etCarnet)
        val etNombreCompleto: EditText = findViewById(R.id.etNombre)
        val etCelular: EditText = findViewById(R.id.etCelular)
        val btGuardarCliente: Button = findViewById(R.id.btGuardarCliente)
        val btActualizarCliente: Button = findViewById(R.id.btActualizarCliente)
        val btEliminarCliente: Button = findViewById(R.id.btEliminarCliente)
        val tvCerrarRCliente: TextView = findViewById(R.id.tvCerrarRCliente)
        tvCerrarRCliente.setOnClickListener{ finish()}

        btGuardarCliente.setOnClickListener {
            val carnet = etCarnet.text.toString()
            val nombreCompleto = etNombreCompleto.text.toString()
            val celular = etCelular.text.toString()

            if (carnet.isNotEmpty() && nombreCompleto.isNotEmpty() && celular.isNotEmpty()) {
                val db = conexionSQL.writableDatabase
                val values = ContentValues().apply {
                    put("carnet", carnet)
                    put("nombreCompleto", nombreCompleto)
                    put("celular", celular)
                }

                //verifcar que la insercion sea correcta a la BD
                val newRowId = db?.insert("Cliente", null, values)
                if (newRowId != null && newRowId > -1) {
                    Toast.makeText(this, "Cliente registrado con éxito", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al registrar el cliente", Toast.LENGTH_SHORT).show()
                }

                db.close()
            } else {
                Toast.makeText(this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show()
            }
        }


        fun actualizarCliente(carnet: String, nombreCompleto: String, celular: String) {
            val db = conexionSQL.writableDatabase
            val valores = ContentValues().apply {
                put("nombreCompleto", nombreCompleto)
                put("celular", celular)
            }
            val cantidad = db.update("Cliente", valores, "carnet=?", arrayOf(carnet))
            if (cantidad > 0) {
                Toast.makeText(this, "Cliente actualizado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al actualizar el cliente", Toast.LENGTH_SHORT).show()
            }
            etCarnet.text.clear()
            etNombreCompleto.text.clear()
            etCelular.text.clear()
            db.close()

        }

        fun eliminarCliente(carnet: String) {
            val db = conexionSQL.writableDatabase
            val cantidad = db.delete("Cliente", "carnet=?", arrayOf(carnet))
            if (cantidad > 0) {
                Toast.makeText(this, "Cliente eliminado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al eliminar el cliente", Toast.LENGTH_SHORT).show()
            }
            etCarnet.text.clear()
            etNombreCompleto.text.clear()
            etCelular.text.clear()
            db.close()
        }

        btActualizarCliente.setOnClickListener {
            val carnet = etCarnet.text.toString()
            if (carnet.isNotEmpty()) {
                actualizarCliente(carnet, etNombreCompleto.text.toString(), etCelular.text.toString())
            } else {
                Toast.makeText(this, "Debe ingresar el carnet del cliente a actualizar", Toast.LENGTH_SHORT).show()
            }
        }

        btEliminarCliente.setOnClickListener {
            val carnet = etCarnet.text.toString()
            if (carnet.isNotEmpty()) {
                eliminarCliente(carnet)
            } else {
                Toast.makeText(this, "Debe ingresar el carnet del cliente a eliminar", Toast.LENGTH_SHORT).show()
            }
        }

    }
}