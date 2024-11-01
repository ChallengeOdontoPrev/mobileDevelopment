
data class AtendenteSignupRequest(
    val email: String,
    val password: String,
    val name: String,
    val rg: String,
    val birthDate: String,
    val role: String = "ATENDENTE", // Valor fixo para cadastro de atendente
    val clinicId: Long
)
