package com.example.semesterproject.pages.farmer

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
import com.example.semesterproject.viewmodels.AuthViewModel
import com.example.semesterproject.viewmodels.BookingViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookingsScreen(
    onBackClick: () -> Unit = {},
    bookingViewModel: BookingViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val bookings by bookingViewModel.bookings
    val currentUser by authViewModel.currentUser

    LaunchedEffect(Unit) {
        authViewModel.getCurrentUser()
        if (currentUser != null) {
            bookingViewModel.fetchFarmerBookings(currentUser!!.uid)
        }
    }

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            bookingViewModel.fetchFarmerBookings(currentUser!!.uid)
        }
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
                text = "My Bookings",
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
                        text = "You have no bookings yet.",
                        fontSize = 16.sp,
                        color = Color(0xFF2D3E2E)
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(bookings) { booking ->
                        BookingCard(booking = booking)
                    }
                }
            }
        }
    }
}

@Composable
fun BookingCard(booking: Booking) {
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
                Text(
                    text = booking.machineName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3E2E)
                )

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

            Text(
                text = "Total: KSh ${booking.totalPrice}",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2D5F3F)
            )
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

