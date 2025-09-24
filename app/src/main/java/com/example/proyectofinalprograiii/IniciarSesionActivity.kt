package com.example.proyectofinalprograiii

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class IniciarSesionActivity : AppCompatActivity() {

    private lateinit var databaseHelper: ConexionSQL
    private lateinit var database: SQLiteDatabase
    private lateinit var exportarExcel: ExportarExcel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciar_sesion)


        val etUsername: EditText = findViewById(R.id.etUser)
        val etPassword: EditText = findViewById(R.id.etPass)
        val btIniciarSesion: Button = findViewById(R.id.btIniciarSesion)
        val tvCerrarIniciarSesion: TextView = findViewById(R.id.tvCerrarInicioSesion)
        tvCerrarIniciarSesion.setOnClickListener{ finish()}

        btIniciarSesion.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Debe insertar el usuario y la contraseña", Toast.LENGTH_SHORT).show()
            } else {

                val db: SQLiteDatabase = databaseHelper.readableDatabase
                val consulta = "SELECT * FROM Vendedor WHERE username = ? AND password = ?"
                val cursor: Cursor = db.rawQuery(consulta, arrayOf(username, password))


                if (cursor.moveToFirst()) {
                    Toast.makeText(this, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show()

                    // Guarda el estado de inicio de sesion
                    val sharedPref = getSharedPreferences("App", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putBoolean("SesionIni", true)
                        apply()
                    }

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {

                    Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
                cursor.close()
                db.close()
            }
        }
    }
}