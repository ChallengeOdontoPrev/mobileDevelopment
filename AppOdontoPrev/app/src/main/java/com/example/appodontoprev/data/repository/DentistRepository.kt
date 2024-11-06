import android.content.Context
import com.example.appodontoprev.data.api.RetrofitConfig

class DentistRepository(private val context: Context) {
    private val apiService = RetrofitConfig.getInstance(context)

    suspend fun getDentists(): Result<List<DentistResponse>> {
        return try {
            val response = apiService.getDentists()
            if (response.isSuccessful && response.body() != null) {
                // Convertendo o objeto único em uma lista
                val dentist = response.body()!!
                Result.success(listOf(dentist))
            } else {
                Result.failure(Exception("Erro ao buscar dentistas: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}