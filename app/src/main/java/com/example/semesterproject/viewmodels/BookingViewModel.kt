package com.example.semesterproject.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.semesterproject.models.Booking
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date

class BookingViewModel : ViewModel() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _bookings = mutableStateOf<List<Booking>>(emptyList())
    val bookings: State<List<Booking>> = _bookings

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    // Fetch bookings for a specific farmer
    fun fetchFarmerBookings(farmerId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val snapshot = db.collection("bookings")
                    .whereEqualTo("farmerId", farmerId)
                    .get()
                    .await()

                _bookings.value = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Booking::class.java)?.copy(id = doc.id)
                }
            } catch (e: Exception) {
                Log.e("BookingViewModel", "Fetch farmer bookings failed", e)
                _errorMessage.value = e.message ?: "Failed to fetch bookings"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Fetch all bookings (for admin)
    fun fetchAllBookings() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val snapshot = db.collection("bookings")
                    .get()
                    .await()

                _bookings.value = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Booking::class.java)?.copy(id = doc.id)
                }
            } catch (e: Exception) {
                Log.e("BookingViewModel", "Fetch all bookings failed", e)
                _errorMessage.value = e.message ?: "Failed to fetch bookings"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Create a new booking
    fun createBooking(
        booking: Booking,
        onSuccess: () -> Unit
    ) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                db.collection("bookings")
                    .add(booking)
                    .await()

                onSuccess()
            } catch (e: Exception) {
                Log.e("BookingViewModel", "Create booking failed", e)
                _errorMessage.value = e.message ?: "Failed to create booking"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Update booking status
    fun updateBookingStatus(bookingId: String, status: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                db.collection("bookings")
                    .document(bookingId)
                    .update("status", status)
                    .await()

                // Refresh bookings
                if (_bookings.value.isNotEmpty()) {
                    val farmerId = _bookings.value.first().farmerId
                    if (farmerId.isNotEmpty()) {
                        fetchFarmerBookings(farmerId)
                    } else {
                        fetchAllBookings()
                    }
                }
            } catch (e: Exception) {
                Log.e("BookingViewModel", "Update booking status failed", e)
                _errorMessage.value = e.message ?: "Failed to update booking"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

