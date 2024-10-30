package com.example.appodontoprev.data.repository

import com.example.appodontoprev.data.api.RetrofitConfig
import com.example.appodontoprev.data.model.request.DentistaSignupRequest
import com.example.appodontoprev.data.model.response.ClinicResponse
import com.example.appodontoprev.data.model.response.SignupResponse

class DentistaRepository {
    private val apiService = RetrofitConfig.apiService

    // Função para cadastrar dentista
    suspend fun cadastrarDentista(dentista: DentistaSignupRequest): Result<SignupResponse> {
        return try {
            val response = apiService.cadastrarDentista(dentista)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Erro no cadastro: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Função para buscar clínicas
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