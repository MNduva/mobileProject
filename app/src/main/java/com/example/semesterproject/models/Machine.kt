package com.example.semesterproject.models

data class Machine(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val description: String = "",
    val pricePerDay: Double = 0.0,
    val imageUri: String = "", // Will store Firebase Storage URI or local path
    val ownerId: String = "",
    val ownerName: String = "",
    val ownerEmail: String = "",
    val ownerPhone: String = "",
    val isAvailable: Boolean = true
) {
    constructor() : this("", "", "", "", 0.0, "", "", "", "", "", true)
}

