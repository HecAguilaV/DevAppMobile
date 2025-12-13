package com.example.sigaapp.data.network

import com.example.sigaapp.BuildConfig
import com.example.sigaapp.data.model.LoginRequest
import com.example.sigaapp.data.model.LoginResponse
import com.example.sigaapp.data.model.PermissionResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultrequest.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.json.JSONObject

class ApiService {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = true
            })
        }
        
        defaultRequest {
            url(BuildConfig.API_BASE_URL)
            contentType(ContentType.Application.Json)
        }
    }

    suspend fun login(email: String, pass: String): Result<LoginResponse> {
        return try {
            val response = client.post("/api/auth/login") { // Path relative to defaultRequest base URL
                setBody(LoginRequest(email, pass))
            }

            if (response.status.isSuccess()) {
                Result.success(response.body())
            } else {
                val errorBody = response.bodyAsText()
                val message = parseErrorMessage(errorBody)
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPermisos(userId: Int, token: String): List<String> {
        return try {
            val response = client.get("/api/saas/usuarios/$userId/permisos") {
                header("Authorization", "Bearer $token")
            }

            if (response.status.isSuccess()) {
                val data = response.body<PermissionResponse>()
                data.permisos
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun parseErrorMessage(jsonString: String): String {
        return try {
            val json = JSONObject(jsonString)
            json.optString("message", "Error desconocido")
        } catch (e: Exception) {
            "Error de conexión"
        }
    }
}
