package com.example.semesterproject.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun LogoHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color(0xFFE8F5E9), shape = RoundedCornerShape(25.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🌾", fontSize = 24.sp)
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "GREENHIRE",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2D5F3F),
            letterSpacing = 1.sp
        )

        Text(
            text = "RENT. HIRE. FARM.",
            fontSize = 6.sp,
            color = Color(0xFF6FA687),
            letterSpacing = 0.5.sp
        )
    }
}
