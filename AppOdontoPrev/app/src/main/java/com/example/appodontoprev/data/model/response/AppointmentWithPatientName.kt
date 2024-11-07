package com.example.appodontoprev.data.model.response

data class AppointmentWithPatientName(
    val id: Long,
    val dateAppointment: String,
    val timeAppointment: String,
    val patientName: String,
    val dentistId: Long,
    val patientId: Long,
    val procedureTypeId: Long
)