package com.example.semesterproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.semesterproject.pages.*
import com.example.semesterproject.pages.admin.*
import com.example.semesterproject.pages.farmer.*
import com.example.semesterproject.viewmodels.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val currentUser by authViewModel.currentUser
    val isSignInSuccess by authViewModel.isSignInSuccess
    val isSignUpSuccess by authViewModel.isSignUpSuccess

    // Check if user is already logged in
    LaunchedEffect(Unit) {
        authViewModel.getCurrentUser()
    }

    // Navigate based on sign-in/sign-up success
    LaunchedEffect(isSignInSuccess, isSignUpSuccess) {
        if (isSignUpSuccess) {
            // After signup, navigate to signin page
            navController.navigate("signin") {
                popUpTo("signup") { inclusive = true }
            }
            authViewModel.resetState()
        } else if (isSignInSuccess) {
            // After signin, navigate to appropriate dashboard
            val user = currentUser
            if (user != null) {
                val destination = if (user.role == "admin") "adminHome" else "farmerHome"
                navController.navigate(destination) {
                    popUpTo("landing") { inclusive = true }
                }
                authViewModel.resetState()
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (currentUser != null) {
            if (currentUser!!.role == "admin") "adminHome" else "farmerHome"
        } else {
            "landing"
        }
    ) {
        // Landing Page
        composable("landing") {
            LandingScreen(
                onSignInClick = { navController.navigate("signin") },
                onSignUpClick = { navController.navigate("signup") }
            )
        }

        // Sign In Page
        composable("signin") {
            SignInScreen(
                onBackClick = { navController.popBackStack() },
                authViewModel = authViewModel
            )
        }

        // Sign Up Page
        composable("signup") {
            SignUpScreen(
                onBackClick = { navController.popBackStack() },
                authViewModel = authViewModel
            )
        }

        // ========== FARMER ROUTES ==========
        composable("farmerHome") {
            FarmerHomeScreen(
                onMachineClick = { machineId ->
                    navController.navigate("machineDetails/$machineId")
                },
                onMyBookingsClick = { navController.navigate("myBookings") },
                onProfileClick = { navController.navigate("farmerProfile") },
                onAboutClick = { navController.navigate("about") },
                onLogoutClick = {
                    authViewModel.signOut()
                    navController.navigate("landing") {
                        popUpTo("landing") { inclusive = true }
                    }
                }
            )
        }

        composable("machineDetails/{machineId}") { backStackEntry ->
            val machineId = backStackEntry.arguments?.getString("machineId") ?: ""
            MachineDetailsScreen(
                machineId = machineId,
                onBackClick = { navController.popBackStack() },
                onBookClick = { machine ->
                    navController.navigate("bookMachine/${machine.id}")
                }
            )
        }

        composable("bookMachine/{machineId}") { backStackEntry ->
            val machineId = backStackEntry.arguments?.getString("machineId") ?: ""
            BookMachineScreen(
                machineId = machineId,
                onBackClick = { navController.popBackStack() },
                onSuccess = {
                    navController.navigate("myBookings") {
                        popUpTo("farmerHome") { inclusive = false }
                    }
                }
            )
        }

        composable("myBookings") {
            MyBookingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("farmerProfile") {
            ProfileScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // ========== ADMIN ROUTES ==========
        composable("adminHome") {
            AdminDashboardScreen(
                onManageMachinesClick = { navController.navigate("manageMachines") },
                onAllBookingsClick = { navController.navigate("allBookings") },
                onManageUsersClick = { navController.navigate("manageUsers") },
                onAddMachineClick = { navController.navigate("addMachine") },
                onProfileClick = { navController.navigate("adminProfile") },
                onAboutClick = { navController.navigate("about") },
                onLogoutClick = {
                    authViewModel.signOut()
                    navController.navigate("landing") {
                        popUpTo("landing") { inclusive = true }
                    }
                }
            )
        }

        composable("manageMachines") {
            ManageMachinesScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("allBookings") {
            AllBookingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("manageUsers") {
            ManageUsersScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("addMachine") {
            AddMachineScreen(
                onBackClick = { navController.popBackStack() },
                onSubmitSuccess = {
                    navController.navigate("manageMachines") {
                        popUpTo("addMachine") { inclusive = false }
                    }
                }
            )
        }

        composable("adminProfile") {
            AdminProfileScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // About screen - accessible from both roles
        composable("about") {
            com.example.semesterproject.pages.AboutScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
