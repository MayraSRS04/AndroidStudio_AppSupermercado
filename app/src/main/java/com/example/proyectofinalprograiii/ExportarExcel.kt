package com.example.proyectofinalprograiii

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Environment
import android.widget.Toast
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook

import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ExportarExcel(private val context: Context, private val database: SQLiteDatabase) {
    fun exportarProductosACategoria() {
        val query = """
            SELECT p.id, p.nombre,p.precioUnitario, c.nombre
            FROM Producto p
            JOIN Categoria c ON p.CategoriaId = c.id
        """
        val cursor = database.rawQuery(query, null)

        // Crear libro y hoja de Excel
        val workbook: Workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Productos")

        // Agregar fila de encabezado
        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("ID Producto")
        headerRow.createCell(1).setCellValue("Nombre Producto")
        headerRow.createCell(2).setCellValue("Precio")
        headerRow.createCell(3).setCellValue("Nombre Categoria")

        // Iterar sobre los datos de la consulta y escribirlos en el archivo Excel
        var rowIndex = 1
        if (cursor.moveToFirst()) {
            do {
                val row = sheet.createRow(rowIndex++)
                row.createCell(0).setCellValue(cursor.getInt(0).toDouble()) // ID Producto
                row.createCell(1).setCellValue(cursor.getString(1))          // Nombre Producto
                row.createCell(2).setCellValue(cursor.getDouble(2))          // Precio
                row.createCell(3).setCellValue(cursor.getString(3))          // Nombre Categoria
            } while (cursor.moveToNext())
        }
        cursor.close()

        // Guardar el archivo en el almacenamiento externo
        val archivoExcel = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Productos_Categorias.xlsx")
        try {
            FileOutputStream(archivoExcel).use { outputStream ->
                workbook.write(outputStream)
            }
            workbook.close()
            Toast.makeText(context, "Archivo guardado en: ${archivoExcel.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}