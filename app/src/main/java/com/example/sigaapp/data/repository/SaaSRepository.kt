package com.example.sigaapp.data.repository

import com.example.sigaapp.data.model.StockItem
import com.example.sigaapp.data.model.Sale
import com.example.sigaapp.data.network.ApiService
import com.example.sigaapp.data.local.SessionManager

class SaaSRepository(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {
    suspend fun getStock(): Result<List<StockItem>> {
        val token = sessionManager.getAccessToken() ?: return Result.failure(Exception("No hay sesión activa"))
        return apiService.getStock(token).map { it.stock }
    }

    suspend fun getVentas(): Result<List<Sale>> {
        val token = sessionManager.getAccessToken() ?: return Result.failure(Exception("No hay sesión activa"))
        return apiService.getVentas(token).map { it.ventas }
    }
}
