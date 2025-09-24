package com.example.proyectofinalprograiii

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ConexionSQL(context: Context)
    : SQLiteOpenHelper(context, "DB.db", null, 1) {

    var tablaVendedor: String= " CREATE TABLE Vendedor (id INTEGER PRIMARY KEY AUTOINCREMENT,nombreCompleto TEXT NOT NULL,username TEXT NOT NULL,password TEXT NOT NULL)"
    var tablaCliente: String= "CREATE TABLE Cliente (id INTEGER PRIMARY KEY AUTOINCREMENT,nombreCompleto TEXT NOT NULL,carnet TEXT NOT NULL,celular TEXT NOT NULL)"
    var tablaCategoria: String="CREATE TABLE Categoria (id INTEGER PRIMARY KEY AUTOINCREMENT,nombre TEXT NOT NULL,descripcion TEXT NOT NULL,descuento DOUBLE NOT NULL)"
    var tablaProducto: String="CREATE TABLE Producto (id INTEGER PRIMARY KEY AUTOINCREMENT,nombre TEXT NOT NULL,marca TEXT NOT NULL,precioUnitario DOUBLE NOT NULL,categoriaId INTEGER NOT NULL,FOREIGN KEY (categoriaId) REFERENCES Categoria (id))"
    var tablaVenta: String="CREATE TABLE Venta (id INTEGER PRIMARY KEY AUTOINCREMENT,vendedorId INTEGER NOT NULL,clienteId INTEGER NOT NULL,fecha TEXT NOT NULL,numFactura TEXT NOT NULL,FOREIGN KEY (vendedorId) REFERENCES Vendedor (id),FOREIGN KEY (clienteId) REFERENCES Cliente (id))"
    var tabladetalleVenta: String="CREATE TABLE DetalleVenta (id INTEGER PRIMARY KEY AUTOINCREMENT,ventaId INTEGER NOT NULL,productoId INTEGER NOT NULL,cantidad INTEGER NOT NULL,precioUnitario REAL NOT NULL,FOREIGN KEY (ventaId) REFERENCES Venta (id),FOREIGN KEY (productoId) REFERENCES Producto (id))"

    var poblarCategoria: String="INSERT INTO Categoria( nombre, descripcion, descuento) VALUES('Limpieza', 'Articulos de limpieza', 0.0), ('Alimentos', 'Comida y alimentos que te quitan el hambre', 0.0), ('Bebidas','Refrescos que quitan la sed', 0.0), ('Juguetes', 'Entretenerse y diversión', 0.0)"
    var poblarProductos: String="INSERT INTO Producto( nombre, marca, precioUnitario, categoriaId) VALUES('Esponja', 'Limpia3000', 8.00, 1), ('Escoba', 'Limpia3000', 20.00, 1), ('Papas', 'Pringles', 22.5, 2), ('Paneton', 'Hipermaxi', 30.00, 2), ('Coca Cola', 'Company Coca Cola', 6.00, 3), ('Ades', 'AdesFresh', 11.50, 3), ('Barbie', 'Mattel', 44.00, 4), ('Slime', 'CompanySlime', 13.5, 4)"
    var poblarCliente: String= "INSERT INTO Cliente( nombreCompleto, carnet, celular) VALUES ('Kevin Cazon', '1234567', '23675498')"
    var poblarVendedor: String="INSERT INTO Vendedor( nombreCompleto, username, password) VALUES('Mayra Rosas', 'May', '123')"


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(tablaVendedor)
        db?.execSQL(tablaCliente)
        db?.execSQL(tablaCategoria)
        db?.execSQL(tablaProducto)
        db?.execSQL(tablaVenta)
        db?.execSQL(tabladetalleVenta)
        db?.execSQL(poblarCategoria)
        db?.execSQL(poblarProductos)
        db?.execSQL(poblarCliente)
        db?.execSQL(poblarVendedor)

    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Vendedor")
        db?.execSQL("DROP TABLE IF EXISTS Cliente")

        onCreate(db)
    }

}