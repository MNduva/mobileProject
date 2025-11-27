package com.example.semesterproject.pages.farmer

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.semesterproject.components.LogoHeader
import com.example.semesterproject.models.Machine
import com.example.semesterproject.viewmodels.MachineViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MachineDetailsScreen(
    machineId: String,
    onBackClick: () -> Unit = {},
    onBookClick: (Machine) -> Unit = {},
    machineViewModel: MachineViewModel = viewModel()
) {
    var machine by remember { mutableStateOf<Machine?>(null) }
    val machines by machineViewModel.machines
    val isLoading by machineViewModel.isLoading

    LaunchedEffect(machineId) {
        // Fetch machines if not already loaded
        if (machineViewModel.machines.value.isEmpty()) {
            machineViewModel.fetchMachines(onlyAvailable = false)
        }
    }

    LaunchedEffect(machineId, machines) {
        machine = machines.find { it.id == machineId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        LogoHeader()
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
        if (isLoading && machine == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF2D5F3F))
            }
        } else if (machine == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Machine not found",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
        } else if (machine != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Machine Image
                if (machine!!.imageUri.isNotEmpty()) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(machine!!.imageUri)
                            .crossfade(true)
                            .build(),
                        contentDescription = "${machine!!.name} image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Crop,
                        loading = {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .background(Color(0xFFF5F5F5)),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(40.dp),
                                    color = Color(0xFF2D5F3F)
                                )
                            }
                        },
                        error = {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .background(Color(0xFFE0E0E0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Image not available",
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .background(Color(0xFFE8F5E9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No Image Available",
                            color = Color(0xFF6FA687),
                            fontSize = 16.sp
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = machine!!.name,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3E2E)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = machine!!.type,
                        fontSize = 18.sp,
                        color = Color(0xFF6FA687)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Price",
                                fontSize = 14.sp,
                                color = Color(0xFF2D3E2E)
                            )
                            Text(
                                text = "KSh ${machine!!.pricePerDay} per day",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2D5F3F)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Description",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2D3E2E)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = machine!!.description.ifEmpty { "No description available" },
                        fontSize = 16.sp,
                        color = Color(0xFF2D3E2E),
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Owner Information",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2D3E2E)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Name: ${machine!!.ownerName}",
                                fontSize = 16.sp,
                                color = Color(0xFF2D3E2E)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Email: ${machine!!.ownerEmail}",
                                fontSize = 16.sp,
                                color = Color(0xFF2D3E2E)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Phone: ${machine!!.ownerPhone}",
                                fontSize = 16.sp,
                                color = Color(0xFF2D3E2E)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { onBookClick(machine!!) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2D5F3F)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Book This Machine",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

