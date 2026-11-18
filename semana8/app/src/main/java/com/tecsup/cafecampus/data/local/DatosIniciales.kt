package com.tecsup.cafecampus.data.local

import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.local.entity.Usuario
import com.tecsup.cafecampus.model.Rol

object DatosIniciales {
    suspend fun sembrar(db: CafeDatabase) {
        // Asegurar que los usuarios siempre existan en la BD sin importar ejecuciones previas
        if (db.usuarioDao().login("admin@tecsup.edu.pe", "Admin00") == null) {
            db.usuarioDao().insertar(
                Usuario(
                    nombre = "Administrador",
                    correo = "admin@tecsup.edu.pe",
                    clave = "Admin00",
                    rol = Rol.ADMIN.name
                )
            )
        }
        if (db.usuarioDao().login("admin@tecsup.edu.pe", "admin") == null) {
            db.usuarioDao().insertar(
                Usuario(
                    nombre = "Administrador",
                    correo = "admin@tecsup.edu.pe",
                    clave = "admin",
                    rol = Rol.ADMIN.name
                )
            )
        }
        if (db.usuarioDao().login("admin@cafe.test", "admin") == null) {
            db.usuarioDao().insertar(
                Usuario(
                    nombre = "Administrador Cafe",
                    correo = "admin@cafe.test",
                    clave = "admin",
                    rol = Rol.ADMIN.name
                )
            )
        }
        if (db.usuarioDao().login("juan@campus.test", "1234") == null) {
            db.usuarioDao().insertar(
                Usuario(
                    nombre = "Juan Pérez",
                    correo = "juan@campus.test",
                    clave = "1234",
                    rol = Rol.CLIENTE.name
                )
            )
        }
        if (db.usuarioDao().login("leonardo.ronda@tecsup.edu.pe", "123456") == null) {
            db.usuarioDao().insertar(
                Usuario(
                    nombre = "Leonardo Ronda",
                    correo = "leonardo.ronda@tecsup.edu.pe",
                    clave = "123456",
                    rol = Rol.CLIENTE.name
                )
            )
        }

        // Sembrar productos si la tabla de productos está vacía
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
                    disponible = true
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
