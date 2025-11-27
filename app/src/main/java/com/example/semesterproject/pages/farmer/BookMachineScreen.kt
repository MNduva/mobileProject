package com.example.semesterproject.pages.farmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.semesterproject.components.LogoHeader
import com.example.semesterproject.models.Booking
import com.example.semesterproject.models.Machine
import com.example.semesterproject.viewmodels.AuthViewModel
import com.example.semesterproject.viewmodels.BookingViewModel
import com.example.semesterproject.viewmodels.MachineViewModel
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookMachineScreen(
    machineId: String,
    onBackClick: () -> Unit = {},
    onSuccess: () -> Unit = {},
    bookingViewModel: BookingViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    machineViewModel: MachineViewModel = viewModel()
) {
    var startDate by remember { mutableStateOf<Date?>(null) }
    var endDate by remember { mutableStateOf<Date?>(null) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    val currentUser by authViewModel.currentUser
    val machines by machineViewModel.machines

    val machine = remember(machineId, machines) {
        machines.find { it.id == machineId }
    }

    LaunchedEffect(Unit) {
        authViewModel.getCurrentUser()
        machineViewModel.fetchMachines()
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
        if (machine == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Book ${machine.name}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3E2E),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Machine Details",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2D3E2E),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Text(
                            text = "Name: ${machine!!.name}",
                            fontSize = 16.sp,
                            color = Color(0xFF2D3E2E)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Type: ${machine.type}",
                            fontSize = 16.sp,
                            color = Color(0xFF2D3E2E)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Price: KSh ${machine.pricePerDay} per day",
                            fontSize = 16.sp,
                            color = Color(0xFF2D3E2E)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Start Date
                OutlinedButton(
                    onClick = { showStartDatePicker = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (startDate != null) {
                            "Start Date: ${
                                android.text.format.DateFormat.format(
                                    "dd/MM/yyyy",
                                    startDate
                                )
                            }"
                        } else {
                            "Select Start Date"
                        },
                        color = Color(0xFF2D5F3F)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // End Date
                OutlinedButton(
                    onClick = { showEndDatePicker = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (endDate != null) {
                            "End Date: ${
                                android.text.format.DateFormat.format(
                                    "dd/MM/yyyy",
                                    endDate
                                )
                            }"
                        } else {
                            "Select End Date"
                        },
                        color = Color(0xFF2D5F3F)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Calculate total price
                val totalPrice = if (startDate != null && endDate != null && machine != null) {
                    val days =
                        ((endDate!!.time - startDate!!.time) / (1000 * 60 * 60 * 24)).toInt() + 1
                    days * machine.pricePerDay
                } else {
                    0.0
                }

                if (totalPrice > 0) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Total Price",
                                fontSize = 16.sp,
                                color = Color(0xFF2D3E2E)
                            )
                            Text(
                                text = "KSh $totalPrice",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2D5F3F)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                Button(
                    onClick = {
                        if (startDate != null && endDate != null && currentUser != null && machine != null) {
                            val booking = Booking(
                                machineId = machine.id,
                                machineName = machine.name,
                                farmerId = currentUser!!.uid,
                                farmerName = "${currentUser!!.firstName} ${currentUser!!.lastName}",
                                farmerEmail = currentUser!!.email,
                                farmerPhone = currentUser!!.phoneNumber,
                                startDate = startDate!!,
                                endDate = endDate!!,
                                totalPrice = totalPrice,
                                status = "pending"
                            )
                            bookingViewModel.createBooking(booking) {
                                onSuccess()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = startDate != null && endDate != null && totalPrice > 0 && machine != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2D5F3F)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Confirm Booking",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }

        // Date Pickers
        if (showStartDatePicker) {
            // Simple date picker - set to today
            LaunchedEffect(showStartDatePicker) {
                startDate = Date() // Set to today for testing
                showStartDatePicker = false
            }
        }

        if (showEndDatePicker) {
            // Simple date picker - set to tomorrow
            LaunchedEffect(showEndDatePicker) {
                val tomorrow = Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)
                endDate = tomorrow // Set to tomorrow for testing
                showEndDatePicker = false
            }
        }
    }
}

