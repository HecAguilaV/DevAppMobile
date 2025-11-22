package com.example.sigaapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.sigaapp.ui.theme.DisabledGray

@Composable
fun DashboardTile(
    title: String,
    icon: ImageVector,
    color: Color,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = if (enabled) color else DisabledGray),
        modifier = Modifier.clickable(enabled = enabled, onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = title, tint = Color.White)
            Text(title, color = Color.White)
        }
    }
}
