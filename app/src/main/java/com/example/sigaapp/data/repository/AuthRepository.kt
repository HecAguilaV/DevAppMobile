package com.example.sigaapp.data.repository

import com.example.sigaapp.data.local.SessionManager
import com.example.sigaapp.data.network.ApiService

class AuthRepository(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {
    suspend fun login(email: String, pass: String): Result<Boolean> {
        val result = apiService.login(email, pass)
        
        return if (result.isSuccess) {
            val response = result.getOrNull()
            if (response != null && response.success && response.accessToken != null && response.user != null) {
                // 1. Guardar sesión básica
                sessionManager.saveAuthSession(
                    token = response.accessToken,
                    userId = response.user.id,
                    role = response.user.rol,
                    nombre = response.user.nombre
                )

                // 2. Obtener y guardar permisos
                val permissions = apiService.getPermisos(response.user.id, response.accessToken)
                sessionManager.savePermissions(permissions)
                
                Result.success(true)
            } else {
                Result.failure(Exception(response?.message ?: "Error desconocido"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Error de conexión"))
        }
    }

    fun logout() {
        sessionManager.clearSession()
    }
    
    fun getUserRole(): String? = sessionManager.getUserRole()
    fun getPermissions(): List<String> = sessionManager.getPermissions()
}
