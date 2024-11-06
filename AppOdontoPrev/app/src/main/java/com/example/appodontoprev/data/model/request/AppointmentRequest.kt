package com.example.appodontoprev.data.model.request

data class AppointmentRequest(
    val dateAppointment: String,
    val timeAppointment: String,
    val dentistId: Long,
    val patientId: Long,
    val procedureTypeId: Long
)

