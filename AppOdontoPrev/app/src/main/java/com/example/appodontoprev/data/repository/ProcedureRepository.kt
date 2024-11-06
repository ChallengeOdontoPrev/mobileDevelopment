import android.content.Context
import com.example.appodontoprev.data.api.RetrofitConfig

class ProcedureRepository(private val context: Context) {
    private val apiService = RetrofitConfig.getInstance(context)

    suspend fun getProcedures(): Result<List<ProcedureResponse>> {
        return try {
            val response = apiService.getProcedures()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Erro ao buscar procedimentos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}