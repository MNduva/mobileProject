package com.example.semesterproject.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMachinesScreen(
    onBackClick: () -> Unit = {},
    onSubmitSuccess: () -> Unit = {}
) {
    var machineName by remember { mutableStateOf("") }
    var machineType by remember { mutableStateOf("") }
    var machineDescription by remember { mutableStateOf("") }
    var pricePerDay by remember { mutableStateOf("") }
    var photoPath by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        // ✅ Inline logo header instead of calling LogoHeader()
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
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Add Machine",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3E2E),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8F5E9), shape = RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                Column {
                    // Machine Details Section
                    Text(
                        text = "Machine Details",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2D3E2E),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Machine name",
                                fontSize = 12.sp,
                                color = Color(0xFF2D3E2E),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            OutlinedTextField(
                                value = machineName,
                                onValueChange = { machineName = it },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedBorderColor = Color(0xFF2D5F3F),
                                    unfocusedBorderColor = Color(0xFF9E9E9E)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Machine type",
                                fontSize = 12.sp,
                                color = Color(0xFF2D3E2E),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            OutlinedTextField(
                                value = machineType,
                                onValueChange = { machineType = it },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedBorderColor = Color(0xFF2D5F3F),
                                    unfocusedBorderColor = Color(0xFF9E9E9E)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Add Machine description",
                                fontSize = 12.sp,
                                color = Color(0xFF2D3E2E),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            OutlinedTextField(
                                value = machineDescription,
                                onValueChange = { machineDescription = it },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedBorderColor = Color(0xFF2D5F3F),
                                    unfocusedBorderColor = Color(0xFF9E9E9E)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Price per day",
                                fontSize = 12.sp,
                                color = Color(0xFF2D3E2E),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            OutlinedTextField(
                                value = pricePerDay,
                                onValueChange = { pricePerDay = it },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedBorderColor = Color(0xFF2D5F3F),
                                    unfocusedBorderColor = Color(0xFF9E9E9E)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Upload a photo of the machine",
                            fontSize = 12.sp,
                            color = Color(0xFF2D3E2E),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        OutlinedTextField(
                            value = photoPath,
                            onValueChange = { photoPath = it },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = Color(0xFF2D5F3F),
                                unfocusedBorderColor = Color(0xFF9E9E9E)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Owner's Details Section
                    Text(
                        text = "Owner's Details",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2D3E2E),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "First Name",
                                fontSize = 12.sp,
                                color = Color(0xFF2D3E2E),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            OutlinedTextField(
                                value = firstName,
                                onValueChange = { firstName = it },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedBorderColor = Color(0xFF2D5F3F),
                                    unfocusedBorderColor = Color(0xFF9E9E9E)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Last Name",
                                fontSize = 12.sp,
                                color = Color(0xFF2D3E2E),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            OutlinedTextField(
                                value = lastName,
                                onValueChange = { lastName = it },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedBorderColor = Color(0xFF2D5F3F),
                                    unfocusedBorderColor = Color(0xFF9E9E9E)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Email",
                                fontSize = 12.sp,
                                color = Color(0xFF2D3E2E),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedBorderColor = Color(0xFF2D5F3F),
                                    unfocusedBorderColor = Color(0xFF9E9E9E)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Phone Number",
                                fontSize = 12.sp,
                                color = Color(0xFF2D3E2E),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            OutlinedTextField(
                                value = phoneNumber,
                                onValueChange = { phoneNumber = it },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedBorderColor = Color(0xFF2D5F3F),
                                    unfocusedBorderColor = Color(0xFF9E9E9E)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Add Machine Button
                    Button(
                        onClick = onSubmitSuccess,
                        modifier = Modifier.align(Alignment.Start),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2D5F3F)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Add Machine",
                            color = Color.White,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
