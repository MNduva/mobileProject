package com.example.semesterproject.pages.farmer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.semesterproject.components.LogoHeader
import com.example.semesterproject.models.Machine
import com.example.semesterproject.viewmodels.MachineViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmerHomeScreen(
    onMachineClick: (String) -> Unit = {},
    onMyBookingsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    machineViewModel: MachineViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val machines by machineViewModel.machines
    val isLoading by machineViewModel.isLoading
    val errorMessage by machineViewModel.errorMessage

    // Fetch machines when screen loads
    LaunchedEffect(Unit) {
        Log.d("FarmerHomeScreen", "Screen loaded, fetching machines...")
        machineViewModel.fetchMachines(onlyAvailable = true)
    }
    
    // Also refresh when screen comes back into focus
    DisposableEffect(Unit) {
        onDispose {
            // Optional: cleanup if needed
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            FarmerDrawerContent(
                onMyBookingsClick = {
                    scope.launch { drawerState.close() }
                    onMyBookingsClick()
                },
                onProfileClick = {
                    scope.launch { drawerState.close() }
                    onProfileClick()
                },
                onAboutClick = {
                    scope.launch { drawerState.close() }
                    onAboutClick()
                },
                onLogoutClick = {
                    scope.launch { drawerState.close() }
                    onLogoutClick()
                },
                onCloseClick = { scope.launch { drawerState.close() } }
            )
        }
    ) {
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
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Open menu",
                                tint = Color(0xFF2D5F3F)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                    modifier = Modifier.height(80.dp)
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(vertical = 24.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Available Machines",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3E2E),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        IconButton(
                            onClick = {
                                Log.d("FarmerHomeScreen", "Manual refresh triggered")
                                machineViewModel.fetchMachines(onlyAvailable = true)
                            },
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color(0xFF2D5F3F)
                                )
                            } else {
                                Text(
                                    text = "🔄",
                                    fontSize = 20.sp
                                )
                            }
                        }
                    }
                }

                // Error message display
                if (errorMessage != null) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Error: $errorMessage",
                                color = Color(0xFFC62828),
                                fontSize = 14.sp,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }

                // Loading state
                if (isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF2D5F3F))
                        }
                    }
                }
                // Empty state
                else if (machines.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No machines available at the moment",
                                fontSize = 16.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                // Machines list
                else {
                    items(machines) { machine ->
                        MachineCard(
                            machine = machine,
                            onClick = { onMachineClick(machine.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FarmerDrawerContent(
    onMyBookingsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAboutClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = Color(0xFFE8F5E9),
        modifier = Modifier.width(280.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onCloseClick) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close drawer",
                        tint = Color(0xFF2D5F3F)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color.White, shape = RoundedCornerShape(30.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🌾", fontSize = 30.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "GREENHIRE",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D5F3F)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            DrawerMenuItem("My Bookings", onClick = onMyBookingsClick)
            Spacer(modifier = Modifier.height(24.dp))
            DrawerMenuItem("Profile", onClick = onProfileClick)
            Spacer(modifier = Modifier.height(24.dp))
            DrawerMenuItem("About", onClick = onAboutClick)
            Spacer(modifier = Modifier.height(24.dp))
            DrawerMenuItem("Logout", onClick = onLogoutClick)
        }
    }
}

@Composable
fun DrawerMenuItem(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF2D3E2E),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 16.dp)
    )
}

@Composable
fun MachineCard(machine: Machine, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            if (machine.imageUri.isNotEmpty()) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(machine.imageUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "${machine.name} image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
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
                                .height(200.dp)
                                .background(Color(0xFFE0E0E0)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Image not available",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                    }
                )
            } else {
                // Placeholder when no image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color(0xFFE8F5E9)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No Image",
                        color = Color(0xFF6FA687),
                        fontSize = 14.sp
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = machine.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2D3E2E)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = machine.type,
                    fontSize = 14.sp,
                    color = Color(0xFF6FA687)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "KSh ${machine.pricePerDay}/day",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D5F3F)
                )
            }
        }
    }
}

