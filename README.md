# AndroidStudio\_AppSupermercado

🛒 Sistema de Gestión de Ventas - Android

Una aplicación móvil desarrollada en Android Studio con Kotlin para la gestión de ventas, clientes, productos y reportes.

📱 Descripción

Esta aplicación permite a los vendedores registrar y gestionar ventas, clientes, productos y categorías. Incluye funcionalidades de autenticación, exportación de datos a Excel y generación de reportes detallados.



🚀 Características

Autenticación de usuarios (inicio de sesión y registro de vendedores)



Gestión de clientes (agregar, actualizar, eliminar)



Gestión de productos (agregar, actualizar, eliminar) con categorías



Registro de ventas con métodos de pago (efectivo, tarjeta, QR)



Reportes de ventas por vendedor, cliente y categoría



Exportación a Excel de productos y categorías



Interfaz intuitiva con navegación entre actividades



🛠️ Tecnologías Utilizadas

Lenguaje: Kotlin



Base de datos: SQLite



Exportación: Apache POI (Excel)



Arquitectura: MVC (Model-View-Controller)



Mínimo SDK: Android 5.0 (API 21)

📦 Estructura del Proyecto

app/

├── java/com/example/proyectofinalprograiii/

│   ├── ConexionSQL.kt           # Manejo de la base de datos

│   ├── MainActivity.kt          # Pantalla principal

│   ├── IniciarSesionActivity.kt # Login

│   ├── LoginActivity.kt         # Registro de vendedores

│   ├── RegistrarClienteActivity.kt

│   ├── RegistrarProductoActivity.kt

│   ├── RegistrarVentaActivity.kt

│   ├── ReporteVentasActivity.kt

│   ├── DetalleVentasVendedorActivity.kt

│   └── ExportarExcel.kt         # Exportación a Excel

└── res/

&nbsp;   ├── layout/                  # Diseños de interfaz

&nbsp;   └── values/                  # Recursos (strings, colors, etc.)



🗄️ Base de Datos

Tablas creadas:

Vendedor (id, nombreCompleto, username, password)



Cliente (id, nombreCompleto, carnet, celular)



Categoria (id, nombre, descripcion, descuento)



Producto (id, nombre, marca, precioUnitario, categoriaId)



Venta (id, vendedorId, clienteId, fecha, numFactura)



DetalleVenta (id, ventaId, productoId, cantidad, precioUnitario)





🧪 Funcionalidades Principales

1\. Autenticación

Registro de nuevos vendedores



Inicio de sesión seguro



2\. Gestión de Clientes

Agregar, editar y eliminar clientes



Búsqueda por carnet de identidad



3\. Gestión de Productos

Organización por categorías



Actualización de precios y marcas



4\. Ventas

Selección de producto, cliente y cantidad



Métodos de pago: Efectivo, Tarjeta, QR



Generación de facturas



5\. Reportes

Ventas por vendedor



Compras por cliente



Productos vendidos por categoría



6\. Exportación

Genera archivo Excel con productos y categorías



📄 Licencia

Este proyecto es de uso educativo. Desarrollado como proyecto final de programación III.

