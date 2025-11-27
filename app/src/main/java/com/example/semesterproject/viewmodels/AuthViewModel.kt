package com.example.semesterproject.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.semesterproject.models.User
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // --- UI States ---
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _isSignUpSuccess = mutableStateOf(false)
    val isSignUpSuccess: State<Boolean> = _isSignUpSuccess

    private val _isSignInSuccess = mutableStateOf(false)
    val isSignInSuccess: State<Boolean> = _isSignInSuccess

    private val _currentUser = mutableStateOf<User?>(null)
    val currentUser: State<User?> = _currentUser

    // --- Sign Up Function ---
    fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String,
        role: String = "farmer" // Default to farmer, admin can be set manually
    ) {
        // 1. Validation
        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || phoneNumber.isBlank() || password.isBlank()) {
            _errorMessage.value = "Please fill in all fields."
            return
        }

        if (password != confirmPassword) {
            _errorMessage.value = "Passwords do not match."
            return
        }

        if (password.length < 6) {
            _errorMessage.value = "Password must be at least 6 characters."
            return
        }

        // 2. Start Process
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                // A. Create Auth User
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user

                if (firebaseUser != null) {
                    // B. Create Firestore User Document with role
                    val newUser = User(
                        uid = firebaseUser.uid,
                        email = email,
                        firstName = firstName.trim(),
                        lastName = lastName.trim(),
                        phoneNumber = phoneNumber.trim(),
                        role = role.lowercase()
                    )

                    db.collection("users")
                        .document(firebaseUser.uid)
                        .set(newUser)
                        .await()

                    _currentUser.value = newUser
                    Log.d("AuthViewModel", "User created successfully: ${firebaseUser.uid}, role: $role")
                    Log.d("AuthViewModel", "User data: firstName=${newUser.firstName}, lastName=${newUser.lastName}, phone=${newUser.phoneNumber}")
                    _isSignUpSuccess.value = true
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "SignUp failed", e)
                _errorMessage.value = when (e) {
                    is FirebaseNetworkException -> {
                        "Network error: Please check your internet connection and try again."
                    }
                    is FirebaseAuthException -> {
                        when (e.errorCode) {
                            "ERROR_INVALID_EMAIL" -> "Invalid email address."
                            "ERROR_EMAIL_ALREADY_IN_USE" -> "This email is already registered."
                            "ERROR_WEAK_PASSWORD" -> "Password is too weak. Please use a stronger password."
                            "ERROR_NETWORK_REQUEST_FAILED" -> "Network error. Please check your connection."
                            else -> e.message ?: "Registration failed. Please try again."
                        }
                    }
                    else -> {
                        if (e.message?.contains("network", ignoreCase = true) == true ||
                            e.message?.contains("timeout", ignoreCase = true) == true ||
                            e.message?.contains("unreachable", ignoreCase = true) == true) {
                            "Network error: Please check your internet connection and try again."
                        } else {
                            e.message ?: "Registration failed. Please try again."
                        }
                    }
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    // --- Sign In Function ---
    // Role is automatically fetched from Firestore database based on user's email
    fun signIn(email: String, password: String) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                // Sign in with Firebase Auth
                val authResult = auth.signInWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user

                if (firebaseUser != null) {
                    // Fetch user data from Firestore to get role from database
                    val userDoc = db.collection("users")
                        .document(firebaseUser.uid)
                        .get()
                        .await()

                    if (userDoc.exists()) {
                        val user = userDoc.toObject(User::class.java)
                        if (user != null) {
                            _currentUser.value = user
                            Log.d("AuthViewModel", "User signed in with role from database: ${user.role}")
                            _isSignInSuccess.value = true
                        } else {
                            _errorMessage.value = "User data could not be parsed."
                        }
                    } else {
                        _errorMessage.value = "User data not found in database. Please sign up first."
                    }
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "SignIn failed", e)
                _errorMessage.value = when (e) {
                    is FirebaseNetworkException -> {
                        "Network error: Please check your internet connection and try again."
                    }
                    is FirebaseAuthException -> {
                        when (e.errorCode) {
                            "ERROR_INVALID_EMAIL" -> "Invalid email address."
                            "ERROR_USER_NOT_FOUND" -> "No account found with this email."
                            "ERROR_WRONG_PASSWORD" -> "Incorrect password."
                            "ERROR_USER_DISABLED" -> "This account has been disabled."
                            "ERROR_TOO_MANY_REQUESTS" -> "Too many failed attempts. Please try again later."
                            "ERROR_NETWORK_REQUEST_FAILED" -> "Network error. Please check your connection."
                            else -> e.message ?: "Sign in failed. Please try again."
                        }
                    }
                    else -> {
                        if (e.message?.contains("network", ignoreCase = true) == true ||
                            e.message?.contains("timeout", ignoreCase = true) == true ||
                            e.message?.contains("unreachable", ignoreCase = true) == true) {
                            "Network error: Please check your internet connection and try again."
                        } else {
                            e.message ?: "Sign in failed. Please try again."
                        }
                    }
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    // --- Get Current User ---
    fun getCurrentUser() {
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            viewModelScope.launch {
                try {
                    val userDoc = db.collection("users")
                        .document(firebaseUser.uid)
                        .get()
                        .await()

                    if (userDoc.exists()) {
                        val user = userDoc.toObject(User::class.java)
                        if (user != null) {
                            _currentUser.value = user
                        }
                    }
                } catch (e: Exception) {
                    Log.e("AuthViewModel", "Get current user failed", e)
                }
            }
        }
    }

    // --- Sign Out ---
    fun signOut() {
        try {
            auth.signOut()
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Sign out error", e)
        }
        _currentUser.value = null
        _isSignInSuccess.value = false
        _isSignUpSuccess.value = false
    }

    fun resetState() {
        _errorMessage.value = null
        _isSignUpSuccess.value = false
        _isSignInSuccess.value = false
        _isLoading.value = false
    }
}

