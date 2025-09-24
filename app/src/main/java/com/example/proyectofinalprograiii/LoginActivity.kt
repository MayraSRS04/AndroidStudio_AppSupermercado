package com.example.proyectofinalprograiii

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.database.sqlite.SQLiteDatabase
import android.database.Cursor
import android.widget.TextView

class LoginActivity : AppCompatActivity() {

    private lateinit var conexionSQL: ConexionSQL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        conexionSQL = ConexionSQL(this)

        val etNombreCompleto: EditText = findViewById(R.id.etNombreC)
        val etUsername: EditText = findViewById(R.id.etUsuario)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val btRegistrarVendedor: Button = findViewById(R.id.btRegistrarVendedor)
        val tvCerrarRVendedor: TextView = findViewById(R.id.tvCerrarRVendedor)
        tvCerrarRVendedor.setOnClickListener{ finish()}

        btRegistrarVendedor.setOnClickListener {
            val nombreCompleto = etNombreCompleto.text.toString()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (nombreCompleto.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Inserta nuevo vendedor
                val db = conexionSQL.writableDatabase
                val values = ContentValues().apply {
                    put("nombreCompleto", nombreCompleto)
                    put("username", username)
                    put("password", password)
                }

                //verifica que el iniciar sesion sea correcta
                //insert devuelve long es el id de la fila recien insertada o -1 si falla
                val newRowId = db.insert("Vendedor", null, values)
                if (newRowId == -1L) {
                    Toast.makeText(this, "No se pudo registrar el vendedor", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Vendedor registrado con éxito", Toast.LENGTH_SHORT).show()
                    etNombreCompleto.text.clear()
                    etUsername.text.clear()
                    etPassword.text.clear()
                }
                db.close()
            }
        }
    }
}