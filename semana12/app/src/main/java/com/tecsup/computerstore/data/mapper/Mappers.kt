package com.tecsup.computerstore.data.mapper

import com.tecsup.computerstore.data.local.ItemEntity
import com.tecsup.computerstore.data.remote.dto.ItemDto
import com.tecsup.computerstore.domain.model.Item

// Conversiones entre ItemDto, ItemEntity e Item
fun ItemDto.toEntity(): ItemEntity {
    return ItemEntity(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        precio = precio,
        categoria = categoria,
        imagenUrl = imagenUrl,
        stock = stock,
        esFavorito = esFavorito
    )
}

fun ItemDto.toDomain(): Item {
    return Item(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        precio = precio,
        categoria = categoria,
        imagenUrl = imagenUrl,
        stock = stock,
        esFavorito = esFavorito
    )
}

fun ItemEntity.toDomain(): Item {
    return Item(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        precio = precio,
        categoria = categoria,
        imagenUrl = imagenUrl,
        stock = stock,
        esFavorito = esFavorito
    )
}

fun Item.toEntity(): ItemEntity {
    return ItemEntity(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        precio = precio,
        categoria = categoria,
        imagenUrl = imagenUrl,
        stock = stock,
        esFavorito = esFavorito
    )
}

fun List<ItemDto>.toEntityList(): List<ItemEntity> = map { it.toEntity() }
fun List<ItemEntity>.toDomainList(): List<Item> = map { it.toDomain() }
