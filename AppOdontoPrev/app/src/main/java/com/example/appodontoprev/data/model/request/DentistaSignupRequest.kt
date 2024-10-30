package com.example.appodontoprev.data.model.request

data class DentistaSignupRequest(
    val email: String,
    val password: String,
    val name: String,
    val rg: String,
    val birthDate: String,
    val role: String = "DENTISTA", // Valor fixo para cadastro de dentista
    val cro: String,
    val clinicId: Long
)