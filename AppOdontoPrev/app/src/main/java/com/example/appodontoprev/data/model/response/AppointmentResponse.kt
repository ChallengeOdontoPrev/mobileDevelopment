data class AppointmentResponse(
    val id: Long,
    val dateAppointment: String,
    val timeAppointment: String,
    val dentistId: Long,
    val patientId: Long,
    val procedureTypeId: Long
)