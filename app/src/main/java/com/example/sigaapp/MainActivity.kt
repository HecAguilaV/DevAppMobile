package com.example.sigaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sigaapp.ui.theme.SIGAAPPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inicializar dependencias manuales (DI simple)
        val sessionManager = com.example.sigaapp.data.local.SessionManager(applicationContext)
        val apiService = com.example.sigaapp.data.network.ApiService()
        val authRepository = com.example.sigaapp.data.repository.AuthRepository(apiService, sessionManager)
        val authViewModelFactory = com.example.sigaapp.ui.viewmodel.AuthViewModelFactory(authRepository)

        setContent {
            SIGAAPPTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val authViewModel: com.example.sigaapp.ui.viewmodel.AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = authViewModelFactory)
                    
                    // Determinar destino inicial
                    val startDestination = if (sessionManager.isLoggedIn()) {
                        val role = sessionManager.getUserRole() ?: "OPERADOR"
                        "dashboard/$role"
                    } else {
                        "login"
                    }

                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("login") { 
                            LoginScreen(navController, authViewModel) 
                        }
                        composable(
                            "dashboard/{userRole}",
                            arguments = listOf(navArgument("userRole") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val roleString = backStackEntry.arguments?.getString("userRole") ?: "OPERADOR"
                            val userRole = try {
                                UserRole.valueOf(roleString.uppercase())
                            } catch (e: Exception) {
                                UserRole.OPERADOR
                            }
                            
                            // Obtener permisos actualizados de la sesión
                            val permissions = sessionManager.getPermissions()
                            
                            DashboardScreen(navController, userRole, permissions)
                        }
                        composable("inventory") { InventoryScreen(navController) }
                        composable("sales") { SalesScreen(navController) }
                        composable("settings") { SettingsScreen(navController) }
                    }
                }
            }
        }
    }
}
