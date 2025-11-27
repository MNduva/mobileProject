package com.example.semesterproject.pages.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.semesterproject.components.LogoHeader
import androidx.compose.material.icons.filled.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onManageMachinesClick: () -> Unit = {},
    onAllBookingsClick: () -> Unit = {},
    onManageUsersClick: () -> Unit = {},
    onAddMachineClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AdminDrawerContent(
                onManageMachinesClick = {
                    scope.launch { drawerState.close() }
                    onManageMachinesClick()
                },
                onAllBookingsClick = {
                    scope.launch { drawerState.close() }
                    onAllBookingsClick()
                },
                onManageUsersClick = {
                    scope.launch { drawerState.close() }
                    onManageUsersClick()
                },
                onAddMachineClick = {
                    scope.launch { drawerState.close() }
                    onAddMachineClick()
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
                    .padding(20.dp)
            ) {
                Text(
                    text = "Admin Dashboard",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3E2E),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                val adminMenuItems = listOf(
                    AdminMenuItem("Manage Machines", Icons.Default.Build, onManageMachinesClick),
                    AdminMenuItem("All Bookings", Icons.Default.Book, onAllBookingsClick),
                    AdminMenuItem("Manage Users", Icons.Default.People, onManageUsersClick),
                    AdminMenuItem("Add Machine", Icons.Default.Add, onAddMachineClick)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(adminMenuItems) { item ->
                        AdminMenuCard(
                            title = item.title,
                            icon = item.icon,
                            onClick = item.onClick
                        )
                    }
                }
            }
        }
    }
}

data class AdminMenuItem(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Composable
fun AdminDrawerContent(
    onManageMachinesClick: () -> Unit,
    onAllBookingsClick: () -> Unit,
    onManageUsersClick: () -> Unit,
    onAddMachineClick: () -> Unit,
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
                    text = "ADMIN",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D5F3F)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            DrawerMenuItem("Manage Machines", onClick = onManageMachinesClick)
            Spacer(modifier = Modifier.height(16.dp))
            DrawerMenuItem("All Bookings", onClick = onAllBookingsClick)
            Spacer(modifier = Modifier.height(16.dp))
            DrawerMenuItem("Manage Users", onClick = onManageUsersClick)
            Spacer(modifier = Modifier.height(16.dp))
            DrawerMenuItem("Add Machine", onClick = onAddMachineClick)
            Spacer(modifier = Modifier.height(16.dp))
            DrawerMenuItem("Profile", onClick = onProfileClick)
            Spacer(modifier = Modifier.height(16.dp))
            DrawerMenuItem("About", onClick = onAboutClick)
            Spacer(modifier = Modifier.height(16.dp))
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
fun AdminMenuCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF2D5F3F),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2D3E2E),
                maxLines = 2
            )
        }
    }
}

