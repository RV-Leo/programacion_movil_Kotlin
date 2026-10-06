package com.ronda.tienda_tecsup

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val categoria: String
)

val productosEjemplo = listOf(
    Producto(1, "Cuaderno universitario", 12.50, "Útiles"),
    Producto(2, "Lapicero azul", 2.00, "Útiles"),
    Producto(3, "Mochila TECSUP", 89.90, "Útiles"),
    Producto(4, "Resaltadores x4", 9.50, "Útiles"),
    Producto(5, "Audífonos inalámbricos", 79.90, "Tecnología"),
    Producto(6, "Mouse óptico", 35.00, "Tecnología"),
    Producto(7, "Memoria USB 64 GB", 29.90, "Tecnología"),
    Producto(8, "Polo TECSUP", 39.90, "Ropa"),
    Producto(9, "Casaca institucional", 99.00, "Ropa"),
    Producto(10, "Gorra TECSUP", 24.90, "Ropa"),
    Producto(11, "Barra de cereal", 3.50, "Snacks"),
    Producto(12, "Agua mineral", 2.50, "Snacks")
)

val categoriasProducto = listOf("Útiles", "Tecnología", "Ropa", "Snacks")
