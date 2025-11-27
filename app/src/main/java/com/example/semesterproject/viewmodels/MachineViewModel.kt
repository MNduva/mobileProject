package com.example.semesterproject.viewmodels

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.semesterproject.models.Machine
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class MachineViewModel : ViewModel() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    private val _machines = mutableStateOf<List<Machine>>(emptyList())
    val machines: State<List<Machine>> = _machines

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    // Fetch all machines (for admin) or only available ones (for farmers)
    fun fetchMachines(onlyAvailable: Boolean = true) {
        _isLoading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            try {
                Log.d("MachineViewModel", "Fetching machines (onlyAvailable=$onlyAvailable)...")
                
                // First, try the filtered query if needed
                var snapshot = if (onlyAvailable) {
                    val filteredQuery = db.collection("machines")
                        .whereEqualTo("isAvailable", true)
                    filteredQuery.get().await()
                } else {
                    db.collection("machines").get().await()
                }
                
                Log.d("MachineViewModel", "Found ${snapshot.documents.size} documents with filter (onlyAvailable=$onlyAvailable)")
                
                // If filtered query returns nothing, try fetching all machines to see if data exists
                if (onlyAvailable && snapshot.documents.isEmpty()) {
                    Log.d("MachineViewModel", "No machines found with isAvailable=true, trying to fetch all machines...")
                    val allSnapshot = db.collection("machines").get().await()
                    Log.d("MachineViewModel", "Found ${allSnapshot.documents.size} total machines in collection")
                    
                    if (allSnapshot.documents.isNotEmpty()) {
                        Log.w("MachineViewModel", "WARNING: Machines exist but none have isAvailable=true. Showing all machines anyway.")
                        snapshot = allSnapshot
                    }
                }

                val machinesList = snapshot.documents.mapNotNull { doc ->
                    try {
                        // Log the raw document data
                        Log.d("MachineViewModel", "Document ID: ${doc.id}")
                        Log.d("MachineViewModel", "Document data: ${doc.data}")
                        
                        // Try to get the machine object
                        val machine = doc.toObject(Machine::class.java)
                        
                        if (machine == null) {
                            Log.e("MachineViewModel", "Failed to convert document ${doc.id} to Machine object")
                            // Try manual mapping as fallback
                            val data = doc.data
                            val manualMachine = Machine(
                                id = doc.id,
                                name = data?.get("name") as? String ?: "",
                                type = data?.get("type") as? String ?: "",
                                description = data?.get("description") as? String ?: "",
                                pricePerDay = (data?.get("pricePerDay") as? Number)?.toDouble() ?: 0.0,
                                imageUri = data?.get("imageUri") as? String ?: "",
                                ownerId = data?.get("ownerId") as? String ?: "",
                                ownerName = data?.get("ownerName") as? String ?: "",
                                ownerEmail = data?.get("ownerEmail") as? String ?: "",
                                ownerPhone = data?.get("ownerPhone") as? String ?: "",
                                isAvailable = data?.get("isAvailable") as? Boolean ?: true
                            )
                            Log.d("MachineViewModel", "Manually mapped machine: ${manualMachine.name}")
                            manualMachine
                        } else {
                            val machineWithId = machine.copy(id = doc.id)
                            Log.d("MachineViewModel", "Successfully loaded machine: ${machineWithId.name}, ImageUri: ${machineWithId.imageUri}, Available: ${machineWithId.isAvailable}")
                            machineWithId
                        }
                    } catch (e: Exception) {
                        Log.e("MachineViewModel", "Error processing document ${doc.id}: ${e.message}")
                        e.printStackTrace()
                        null
                    }
                }

                _machines.value = machinesList
                Log.d("MachineViewModel", "Successfully loaded ${machinesList.size} machines into list")
                
                if (machinesList.isEmpty() && snapshot.documents.isNotEmpty()) {
                    Log.w("MachineViewModel", "WARNING: Found ${snapshot.documents.size} documents but mapped to 0 machines. Check field names!")
                }
            } catch (e: Exception) {
                Log.e("MachineViewModel", "Fetch machines failed", e)
                Log.e("MachineViewModel", "Error: ${e.javaClass.simpleName}: ${e.message}")
                e.printStackTrace()
                _errorMessage.value = e.message ?: "Failed to fetch machines. Please check your internet connection."
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Add machine with image upload
    fun addMachine(
        machine: Machine,
        imageUri: Uri?,
        imageUrlString: String? = null,
        onSuccess: () -> Unit
    ) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                var imageUrl = machine.imageUri

                // Use provided URL string if available (from predefined list)
                if (!imageUrlString.isNullOrBlank()) {
                    imageUrl = imageUrlString
                    Log.d("MachineViewModel", "Using provided image URL: $imageUrl")
                }
                // Upload image if provided (from gallery)
                else if (imageUri != null) {
                    Log.d("MachineViewModel", "Uploading image to Firebase Storage...")
                    val imageRef = storage.reference
                        .child("machine_images/${UUID.randomUUID()}.jpg")
                    
                    val uploadTask = imageRef.putFile(imageUri).await()
                    imageUrl = uploadTask.storage.downloadUrl.await().toString()
                    Log.d("MachineViewModel", "Image uploaded successfully: $imageUrl")
                } else {
                    Log.d("MachineViewModel", "No image provided, using empty imageUri")
                }

                val machineWithImage = machine.copy(imageUri = imageUrl)
                
                Log.d("MachineViewModel", "Saving machine to Firestore: name=${machineWithImage.name}, type=${machineWithImage.type}, price=${machineWithImage.pricePerDay}")

                val docRef = db.collection("machines")
                    .add(machineWithImage)
                    .await()

                Log.d("MachineViewModel", "Machine added successfully with ID: ${docRef.id}")
                
                onSuccess()
                fetchMachines(onlyAvailable = false) // Refresh list
            } catch (e: Exception) {
                Log.e("MachineViewModel", "Add machine failed", e)
                Log.e("MachineViewModel", "Error details: ${e.javaClass.simpleName}: ${e.message}")
                e.printStackTrace()
                _errorMessage.value = e.message ?: "Failed to add machine. Please check your internet connection and try again."
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Delete machine
    fun deleteMachine(machineId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                db.collection("machines")
                    .document(machineId)
                    .delete()
                    .await()

                fetchMachines() // Refresh list
            } catch (e: Exception) {
                Log.e("MachineViewModel", "Delete machine failed", e)
                _errorMessage.value = e.message ?: "Failed to delete machine"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

