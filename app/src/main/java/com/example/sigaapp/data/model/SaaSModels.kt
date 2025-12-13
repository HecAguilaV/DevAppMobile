package com.example.sigaapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val nombre: String,
    val descripcion: String? = null,
    val precio: Int, // Asumiendo precio entero en pesos
    val codigo: String? = null
)

@Serializable
data class ProductosListResponse(
    val success: Boolean,
    val productos: List<Product> = emptyList()
)

@Serializable
data class StockItem(
    val id: Int,
    val producto_id: Int,
    val local_id: Int,
    val cantidad: Int,
    val min_stock: Int,
    val producto: Product? = null // Si viene anidado
)

@Serializable
data class StockListResponse(
    val success: Boolean,
    val stock: List<StockItem> = emptyList()
)

@Serializable
data class Sale(
    val id: Int,
    val fecha: String, // ISO date
    val total: Int,
    val items: Int, // Cantidad de items o calculado
    val local_id: Int,
    val local_nombre: String? = null // Si la API lo devuelve
)

@Serializable
data class VentasListResponse(
    val success: Boolean,
    val ventas: List<Sale> = emptyList()
)

@Serializable
data class ProductRequest(
    val nombre: String,
    val precio: Int,
    val descripcion: String? = null
)

@Serializable
data class ProductResponse(
    val success: Boolean,
    val producto: Product
)

@Serializable
data class Local(
    val id: Int,
    val nombre: String,
    val direccion: String? = null
)

@Serializable
data class LocalesResponse(
    val success: Boolean,
    val locales: List<Local> = emptyList()
)
