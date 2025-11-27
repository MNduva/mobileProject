package com.example.semesterproject.pages.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.semesterproject.components.LogoHeader
import com.example.semesterproject.models.Machine
import com.example.semesterproject.viewmodels.MachineViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageMachinesScreen(
    onBackClick: () -> Unit = {},
    machineViewModel: MachineViewModel = viewModel()
) {
    val machines by machineViewModel.machines

    LaunchedEffect(Unit) {
        machineViewModel.fetchMachines(onlyAvailable = false) // Admin sees all machines
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
                text = "Manage Machines",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3E2E),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            if (machines.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color(0xFFE8F5E9), shape = RoundedCornerShape(16.dp))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No machines found.",
                        fontSize = 16.sp,
                        color = Color(0xFF2D3E2E)
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(machines) { machine ->
                        AdminMachineCard(
                            machine = machine,
                            onDelete = { machineViewModel.deleteMachine(machine.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AdminMachineCard(machine: Machine, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (machine.imageUri.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(machine.imageUri),
                    contentDescription = "${machine.name} image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = machine.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3E2E)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = machine.type,
                    fontSize = 14.sp,
                    color = Color(0xFF6FA687)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "KSh ${machine.pricePerDay}/day",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2D5F3F)
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color(0xFFEF5350)
                )
            }
        }
    }
}

