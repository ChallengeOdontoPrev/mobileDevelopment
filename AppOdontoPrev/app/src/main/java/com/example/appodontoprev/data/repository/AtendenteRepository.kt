import com.example.appodontoprev.data.api.RetrofitConfig
import com.example.appodontoprev.data.model.response.ClinicResponse
import com.example.appodontoprev.data.model.response.SignupResponse

class AtendenteRepository {
    private val apiService = RetrofitConfig.apiService

    suspend fun cadastrarAtendente(atendente: AtendenteSignupRequest): Result<SignupResponse> {
        return try {
            val response = apiService.cadastrarAtendente(atendente)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Erro no cadastro: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getClinicas(): Result<List<ClinicResponse>> {
        return try {
            val response = apiService.getClinicas()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Erro ao buscar clínicas: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}