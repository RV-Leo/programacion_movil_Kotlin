package com.tecsup.cafecampus.data.local

import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.local.entity.Usuario
import com.tecsup.cafecampus.model.Rol

object DatosIniciales {
    suspend fun sembrar(db: CafeDatabase) {
        if (db.usuarioDao().contar() == 0) {
            val usuarios = listOf(
                Usuario(
                    nombre = "Leonardo Ronda",
                    correo = "leonardo.ronda@tecsup.edu.pe",
                    clave = "123456",
                    rol = Rol.CLIENTE.name
                ),
                Usuario(
                    nombre = "Administrador",
                    correo = "admin@tecsup.edu.pe",
                    clave = "Admin00",
                    rol = Rol.ADMIN.name
                )
            )
            for (u in usuarios) {
                db.usuarioDao().insertar(u)
            }
        }

        if (db.productoDao().contar() == 0) {
            val productos = listOf(
                Producto(
                    nombre = "Café Americano",
                    descripcion = "Café pasado espresso concentrado con agua caliente",
                    categoria = "Bebidas",
                    precio = 4.50,
                    disponible = true
                ),
                Producto(
                    nombre = "Café con Leche",
                    descripcion = "Espresso recién pasado combinado con leche al vapor",
                    categoria = "Bebidas",
                    precio = 5.50,
                    disponible = true,
                ),
                Producto(
                    nombre = "Capuchino",
                    descripcion = "Espresso con abundante espuma de leche suave",
                    categoria = "Bebidas",
                    precio = 6.00,
                    disponible = true
                ),
                Producto(
                    nombre = "Sándwich de Pollo",
                    descripcion = "Pechuga deshilachada con verduras frescas y mayonesa",
                    categoria = "Comidas",
                    precio = 7.50,
                    disponible = true
                ),
                Producto(
                    nombre = "Empanada de Carne",
                    descripcion = "Masa horneada crujiente rellena de carne picada sazonada",
                    categoria = "Comidas",
                    precio = 5.00,
                    disponible = true
                ),
                Producto(
                    nombre = "Muffin de Arándanos",
                    descripcion = "Keke esponjoso casero horneado con arándanos frescos",
                    categoria = "Postres",
                    precio = 4.00,
                    disponible = true
                )
            )
            for (p in productos) {
                db.productoDao().insertar(p)
            }
        }
    }
}
