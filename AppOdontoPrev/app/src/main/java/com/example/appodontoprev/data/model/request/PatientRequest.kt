// PatientRequest.kt
package com.example.appodontoprev.data.model.request

data class PatientRequest(
    val name: String,
    val rg: String,
    val birthDate: String,
    val numCard: Long
)