package com.example.sigaapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sigaapp.ui.theme.*

enum class UserRole { ADMIN, OPERATOR }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController, userRole: UserRole) {

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var chatMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("SIGA")
                        Text("Tienda Principal", style = MaterialTheme.typography.bodySmall)
                    }
                },
                actions = {
                    Text(if (userRole == UserRole.ADMIN) "AD" else "OP", modifier = Modifier.padding(end = 16.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryDark,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { 
                    chatMessage = "Hola, soy SIGA. ¿En qué puedo ayudarte?"
                    showBottomSheet = true
                 },
                containerColor = AccentCyan,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = "AI Assistant")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = padding,
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                DashboardTile(
                    title = "Inventario",
                    icon = Icons.Default.Inventory,
                    color = EmeraldOps,
                    enabled = true,
                    onClick = {
                        chatMessage = "Hola Administrador, el stock está al 95% valorizado en $875k."
                        showBottomSheet = true
                    }
                )
            }
            item {
                 DashboardTile(
                    title = "Documentos",
                    icon = Icons.Default.Description,
                    color = AccentCyan,
                    enabled = userRole == UserRole.ADMIN,
                    onClick = { 
                        chatMessage = "Accediendo a documentos..."
                        showBottomSheet = true
                    }
                )
            }
            item {
                DashboardTile(
                    title = "Gastos",
                    icon = Icons.Default.TrendingDown,
                    color = AlertRed,
                    enabled = userRole == UserRole.ADMIN,
                    onClick = { 
                        chatMessage = "Análisis de gastos: Se ha detectado un aumento del 15% en costos de logística."
                        showBottomSheet = true
                    }
                )
            }
            item {
                DashboardTile(
                    title = "Ventas",
                    icon = Icons.Default.TrendingUp,
                    color = AccentCyan,
                    enabled = userRole == UserRole.ADMIN,
                    onClick = { 
                        chatMessage = "Reporte de ventas: Hoy se han registrado 52 ventas por un total de $12,500."
                        showBottomSheet = true
                    }
                )
            }
            // Mini Grid
            item { DashboardTile("Chat", Icons.Default.Chat, EmeraldOps, true, {}) }
            item { DashboardTile("Ajustes", Icons.Default.Settings, EmeraldOps, userRole == UserRole.ADMIN, {}) }
            item { DashboardTile("Soporte", Icons.Default.Support, EmeraldOps, true, {}) }
            item { 
                DashboardTile("Salir", Icons.Default.ExitToApp, AlertRed, true) {
                    navController.navigate("login") { popUpTo("login") { inclusive = true } }
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                Box(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
                    Text(chatMessage)
                }
            }
        }
    }
}