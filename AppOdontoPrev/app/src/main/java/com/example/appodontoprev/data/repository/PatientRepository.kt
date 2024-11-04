package com.example.appodontoprev.data.repository

import android.content.Context
import com.example.appodontoprev.data.api.RetrofitConfig
import com.example.appodontoprev.data.model.response.PatientResponse
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class PatientRepository(private val context: Context) {
    private val apiService = RetrofitConfig.getInstance(context)

    suspend fun getPatientByRg(rg: String): Result<PatientResponse> {
        return try {
            val response = apiService.getPatientByRg(rg)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = when (response.code()) {
                    404 -> "Paciente não encontrado"
                    400 -> "Requisição inválida"
                    401 -> "Não autorizado. Faça login novamente."
                    403 -> "Acesso negado. Você não tem permissão para esta ação."
                    500 -> "Erro interno do servidor"
                    else -> errorBody ?: "Erro desconhecido: ${response.code()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is UnknownHostException -> "Erro de conexão. Verifique sua internet."
                is SocketTimeoutException -> "Tempo de conexão esgotado. Tente novamente."
                else -> e.message ?: "Erro desconhecido"
            }
            Result.failure(Exception(errorMessage))
        }
    }
}