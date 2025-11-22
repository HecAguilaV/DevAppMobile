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
        setContent {
            SIGAAPPTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") { LoginScreen(navController) }
                        composable(
                            "dashboard/{userRole}",
                            arguments = listOf(navArgument("userRole") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val userRole = UserRole.valueOf(backStackEntry.arguments?.getString("userRole") ?: "OPERATOR")
                            DashboardScreen(navController, userRole)
                        }
                    }
                }
            }
        }
    }
}
