package com.ronda.tienda_tecsup

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val categoria: String
)

val productosEjemplo = listOf(
    Producto(1, "Audifonos", 89.00, "Tecnología"),
    Producto(2, "Smartwatch", 199.00, "Tecnología"),
    Producto(3, "Funda celular", 25.00, "Tecnología"),
    Producto(4, "Cuaderno universitario", 12.50, "Útiles"),
    Producto(5, "Lapicero azul", 2.00, "Útiles"),
    Producto(6, "Mochila TECSUP", 89.90, "Útiles"),
    Producto(7, "Resaltadores x4", 9.50, "Útiles"),
    Producto(8, "Mouse óptico", 35.00, "Tecnología"),
    Producto(9, "Memoria USB 64 GB", 29.90, "Tecnología"),
    Producto(10, "Polo TECSUP", 39.90, "Ropa"),
    Producto(11, "Casaca institucional", 99.00, "Ropa"),
    Producto(12, "Gorra TECSUP", 24.90, "Ropa"),
    Producto(13, "Barra de cereal", 3.50, "Snacks"),
    Producto(14, "Agua mineral", 2.50, "Snacks")
)

val categoriasProducto = listOf("Tecnología", "Útiles", "Ropa", "Snacks")
