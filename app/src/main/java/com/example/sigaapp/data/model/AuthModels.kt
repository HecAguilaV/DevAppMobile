package com.example.sigaapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val success: Boolean,
    @SerialName("access_token") val accessToken: String? = null,
    @SerialName("refresh_token") val refreshToken: String? = null,
    val user: User? = null,
    val message: String? = null
)

@Serializable
data class User(
    val id: Int,
    val email: String,
    val rol: String,
    val nombre: String? = null
)

@Serializable
data class PermissionResponse(
    val success: Boolean,
    val permisos: List<String>
)
