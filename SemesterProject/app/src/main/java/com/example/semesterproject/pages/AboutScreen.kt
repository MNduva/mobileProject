package com.example.semesterproject.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "About GreenHire",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D5F3F),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF2D5F3F)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                modifier = Modifier.height(80.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "GreenHire is a mobile platform designed to make renting and hiring agricultural equipment easier, faster, and more affordable for farmers and service providers.",
                fontSize = 16.sp,
                color = Color(0xFF2D3E2E),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8F5E9), shape = RoundedCornerShape(16.dp))
                    .padding(24.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Contact Information",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2D5F3F),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    ContactItem(name = "Ian Chomba", email = "ian.chomba@strathmore.edu")
                    ContactItem(name = "Bravin Oduor", email = "bravin.oduor@strathmore.edu")
                    ContactItem(name = "John Kimathi", email = "john.mwiti@strathmore.edu")
                }
            }
        }
    }
}

@Composable
fun ContactItem(name: String, email: String) {
    Column {
        Text(
            text = name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2D3E2E)
        )
        Text(
            text = email,
            fontSize = 14.sp,
            color = Color(0xFF6FA687)
        )
    }
}
