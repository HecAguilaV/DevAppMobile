package com.example.sigaapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sigaapp.ui.theme.*
import java.text.NumberFormat
import java.util.*

// Datos de ejemplo para ventas
data class Sale(
    val id: String,
    val date: String,
    val amount: Double,
    val items: Int,
    val location: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesScreen(navController: NavController) {
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }
    
    // Datos hardcodeados de ejemplo
    val sales = remember {
        listOf(
            Sale("1", "Hoy", 125000.0, 8, "ITR"),
            Sale("2", "Ayer", 98000.0, 6, "ITR"),
            Sale("3", "15 Ene", 156000.0, 10, "ITR"),
            Sale("4", "14 Ene", 87000.0, 5, "ITR"),
            Sale("5", "13 Ene", 142000.0, 9, "ITR")
        )
    }

    val totalToday = sales.firstOrNull()?.amount ?: 0.0
    val totalWeek = sales.sumOf { it.amount }
    val avgSale = sales.map { it.amount }.average()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Ventas",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AccentTurquoise
                )
            )
        },
        containerColor = Background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Resumen de ventas
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = AccentTurquoise
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = currencyFormat.format(totalToday),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = White
                            )
                            Text(
                                text = "Hoy",
                                style = MaterialTheme.typography.bodySmall,
                                color = White.copy(alpha = 0.9f)
                            )
                        }
                    }
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = AccentCyan
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = currencyFormat.format(totalWeek),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = White
                            )
                            Text(
                                text = "Esta Semana",
                                style = MaterialTheme.typography.bodySmall,
                                color = White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = EmeraldOps
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Promedio por Venta",
                                style = MaterialTheme.typography.bodyMedium,
                                color = White.copy(alpha = 0.9f)
                            )
                            Text(
                                text = currencyFormat.format(avgSale),
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = White
                            )
                        }
                        Icon(
                            Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            // Lista de ventas recientes
            item {
                Text(
                    text = "Ventas Recientes",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = TextPrimary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(sales) { sale ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = SurfaceLight
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        color = AccentTurquoise,
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.PointOfSale,
                                    contentDescription = null,
                                    tint = White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = sale.date,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${sale.items} items • ${sale.location}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }
                        Text(
                            text = currencyFormat.format(sale.amount),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = AccentTurquoise
                        )
                    }
                }
            }
        }
    }
}

