package com.example.semesterproject.data

object MachineImageRepository {
    fun getAllImages(): List<String> {
        return MachineImageUrls.getAllImages()
    }
}
