package com.example.appodontoprev.data.model.request

interface SignupRequest {
    val email: String
    val password: String
    val name: String
    val rg: String
    val birthDate: String
    val role: String
    val clinicId: Long
}