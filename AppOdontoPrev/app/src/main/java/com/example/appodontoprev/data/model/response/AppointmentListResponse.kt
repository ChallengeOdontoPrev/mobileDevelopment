data class AppointmentListResponse(
    val id: Long,
    val dateAppointment: String,
    val timeAppointment: String,
    val patient: String,
    val clinic: String,
    val procedureType: String
)