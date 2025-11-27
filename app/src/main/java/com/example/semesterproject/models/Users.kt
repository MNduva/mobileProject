package com.example.semesterproject.models

data class User(
    val uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val role: String = "farmer" // "farmer" or "admin"
) {
    constructor() : this("", "", "", "", "", "farmer")
}

