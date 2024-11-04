package com.example.appodontoprev.data.model.response

data class PatientResponse(
    val id: Long,
    val name: String,
    val rg: String,
    val birthDate: String,
    val numCard: Long,
    val createdAt: String
)