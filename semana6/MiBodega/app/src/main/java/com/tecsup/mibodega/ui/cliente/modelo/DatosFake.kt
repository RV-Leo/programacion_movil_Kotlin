package com.tecsup.mibodega.ui.cliente.modelo

import com.tecsup.mibodega.R

val listaCategorias = listOf("Todos", "Bebidas", "Abarrotes", "Lácteos", "Snacks", "Limpieza")

val listaProductosFake = listOf(
    Producto(
        id = 1,
        nombre = "Arroz Costeño",
        descripcion = "Arroz extra, grano largo, ideal para el día a día.",
        precio = 4.50,
        categoria = "Abarrotes",
        imagenRes = R.drawable.arroz
    ),
    Producto(
        id = 2,
        nombre = "Aceite Primor",
        descripcion = "Aceite vegetal 1 L, alto en vitamina E.",
        precio = 8.90,
        categoria = "Abarrotes",
        imagenRes = R.drawable.aceite_primor
    ),
    Producto(
        id = 3,
        nombre = "Leche Gloria",
        descripcion = "Leche evaporada entera 1 L.",
        precio = 5.20,
        categoria = "Lácteos",
        imagenRes = R.drawable.leche_gloria
    ),
    Producto(
        id = 4,
        nombre = "Galleta Oreo",
        descripcion = "Galletas de chocolate rellenas 126 g.",
        precio = 3.50,
        categoria = "Snacks",
        imagenRes = R.drawable.galleta_oreo
    ),
    Producto(
        id = 5,
        nombre = "Coca-Cola Original",
        descripcion = "Bebida gaseosa sabor cola. Ideal para compartir en familia.",
        precio = 6.50,
        categoria = "Bebidas",
        imagenRes = R.drawable.coca_cola
    ),
    Producto(
        id = 6,
        nombre = "Agua San Luis",
        descripcion = "Agua sin gas en botella de 625 ml.",
        precio = 1.80,
        categoria = "Bebidas",
        imagenRes = R.drawable.agua_sanluis
    ),
    Producto(
        id = 7,
        nombre = "Fideos Don Vittorio",
        descripcion = "Pasta corta enriquecida de 500 g.",
        precio = 4.20,
        categoria = "Abarrotes",
        imagenRes = R.drawable.pasta_donvictorio
    ),
    Producto(
        id = 8,
        nombre = "Atún Florida",
        descripcion = "Atún en aceite vegetal en lata de 170 g.",
        precio = 6.90,
        categoria = "Abarrotes",
        imagenRes = R.drawable.atun_florida
    ),
    Producto(
        id = 9,
        nombre = "Yogurt Laive",
        descripcion = "Yogurt de fresa en botella de 1 L.",
        precio = 7.50,
        categoria = "Lácteos",
        imagenRes = R.drawable.yogurt_laive
    ),
    Producto(
        id = 10,
        nombre = "Queso Edam",
        descripcion = "Queso Edam en presentación de 200 g.",
        precio = 9.80,
        categoria = "Lácteos",
        imagenRes = R.drawable.queso_edam
    ),
    Producto(
        id = 11,
        nombre = "Papas Lays",
        descripcion = "Papas fritas clásicas en bolsa de 150 g.",
        precio = 5.50,
        categoria = "Snacks",
        imagenRes = R.drawable.papas_lays
    ),
    Producto(
        id = 12,
        nombre = "Detergente Bolívar",
        descripcion = "Detergente en polvo para ropa en bolsa de 800 g.",
        precio = 8.40,
        categoria = "Limpieza",
        imagenRes = R.drawable.deteregente_bolivar
    )
)
