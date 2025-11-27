package com.example.semesterproject.models

import java.util.Date

data class Booking(
    val id: String = "",
    val machineId: String = "",
    val machineName: String = "",
    val farmerId: String = "",
    val farmerName: String = "",
    val farmerEmail: String = "",
    val farmerPhone: String = "",
    val startDate: Date = Date(),
    val endDate: Date = Date(),
    val totalPrice: Double = 0.0,
    val status: String = "pending" // "pending", "confirmed", "completed", "cancelled"
) {
    constructor() : this(
        id = "",
        machineId = "",
        machineName = "",
        farmerId = "",
        farmerName = "",
        farmerEmail = "",
        farmerPhone = "",
        startDate = Date(),
        endDate = Date(),
        totalPrice = 0.0,
        status = "pending"
    )
}

