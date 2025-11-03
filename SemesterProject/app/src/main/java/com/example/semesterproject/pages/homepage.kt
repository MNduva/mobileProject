package com.example.semesterproject.pages

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.semesterproject.R
import com.example.semesterproject.components.LogoHeader
import kotlinx.coroutines.launch

// Data model for a machine item
data class Machine(
    val id: Int,
    val name: String,
    val imageRes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMachineClick: (Int) -> Unit = {},
    onCheckBookingsClick: () -> Unit = {},
    onAddMachinesClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Example static machine data (replace with your ViewModel data if needed)
    val machines = listOf(
        Machine(1, "Tractor", R.drawable.machine_1),
        Machine(2, "Plough", R.drawable.machine_2),
        Machine(3, "Harvester", R.drawable.machine_3)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onCheckBookingsClick = {
                    scope.launch { drawerState.close() }
                    onCheckBookingsClick()
                },
                onAddMachinesClick = {
                    scope.launch { drawerState.close() }
                    onAddMachinesClick()
                },
                onAboutClick = {
                    scope.launch { drawerState.close() }
                    onAboutClick()
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

@Composable
fun DrawerContent(
    onCheckBookingsClick: () -> Unit,
    onAddMachinesClick: () -> Unit,
    onAboutClick: () -> Unit,
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
            // Close Button
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

            // Drawer Header
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

            // Menu Items
            DrawerMenuItem("Check Bookings", onClick = onCheckBookingsClick)
            Spacer(modifier = Modifier.height(24.dp))
            DrawerMenuItem("Add Machines", onClick = onAddMachinesClick)
            Spacer(modifier = Modifier.height(24.dp))
            DrawerMenuItem("About", onClick = onAboutClick)
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
            Image(
                painter = painterResource(id = machine.imageRes),
                contentDescription = "${machine.name} image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )

            Text(
                text = machine.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2D3E2E),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
        }
    }
}
