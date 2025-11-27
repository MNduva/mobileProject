package com.example.semesterproject.pages.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.semesterproject.components.LogoHeader
import com.example.semesterproject.models.Booking
import com.example.semesterproject.viewmodels.BookingViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllBookingsScreen(
    onBackClick: () -> Unit = {},
    bookingViewModel: BookingViewModel = viewModel()
) {
    val bookings by bookingViewModel.bookings

    LaunchedEffect(Unit) {
        bookingViewModel.fetchAllBookings()
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = "All Bookings",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3E2E),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            if (bookings.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color(0xFFE8F5E9), shape = RoundedCornerShape(16.dp))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No bookings found.",
                        fontSize = 16.sp,
                        color = Color(0xFF2D3E2E)
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(bookings) { booking ->
                        AdminBookingCard(
                            booking = booking,
                            bookingViewModel = bookingViewModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AdminBookingCard(
    booking: Booking,
    bookingViewModel: BookingViewModel
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = booking.machineName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3E2E)
                    )
                    Text(
                        text = "By: ${booking.farmerName}",
                        fontSize = 14.sp,
                        color = Color(0xFF6FA687)
                    )
                }
                StatusChip(status = booking.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Start: ${dateFormat.format(booking.startDate)}",
                fontSize = 14.sp,
                color = Color(0xFF2D3E2E)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "End: ${dateFormat.format(booking.endDate)}",
                fontSize = 14.sp,
                color = Color(0xFF2D3E2E)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total: KSh ${booking.totalPrice}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2D5F3F)
                )

                Text(
                    text = "Phone: ${booking.farmerPhone}",
                    fontSize = 14.sp,
                    color = Color(0xFF2D3E2E)
                )
            }

            // Approve/Reject buttons for pending bookings
            if (booking.status.lowercase() == "pending") {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            bookingViewModel.updateBookingStatus(booking.id, "confirmed")
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF66BB6A)
                        )
                    ) {
                        Text("Approve", color = Color.White)
                    }
                    Button(
                        onClick = {
                            bookingViewModel.updateBookingStatus(booking.id, "cancelled")
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEF5350)
                        )
                    ) {
                        Text("Reject", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val (color, text) = when (status.lowercase()) {
        "pending" -> Color(0xFFFFA726) to "Pending"
        "confirmed" -> Color(0xFF66BB6A) to "Confirmed"
        "completed" -> Color(0xFF42A5F5) to "Completed"
        "cancelled" -> Color(0xFFEF5350) to "Cancelled"
        else -> Color.Gray to status
    }

    Surface(
        color = color,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

