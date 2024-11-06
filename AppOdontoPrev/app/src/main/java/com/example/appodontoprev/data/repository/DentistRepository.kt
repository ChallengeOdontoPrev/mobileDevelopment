package com.example.appodontoprev.data.repository

import DentistResponse
import android.content.Context
import com.example.appodontoprev.data.api.RetrofitConfig
import com.example.appodontoprev.data.model.request.DentistaSignupRequest
import com.example.appodontoprev.data.model.response.ClinicResponse
import com.example.appodontoprev.data.model.response.SignupResponse
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class DentistRepository(private val context: Context) {
    // Usar instância com autenticação para buscar dentistas
    private val apiService = RetrofitConfig.getInstance(context)
    // Usar instância sem autenticação para operações de cadastro
    private val apiServiceNoAuth = RetrofitConfig.getInstanceNoAuth(context)

    suspend fun cadastrarDentista(dentista: DentistaSignupRequest): Result<SignupResponse> {
        return try {
            val response = apiServiceNoAuth.cadastrarDentista(dentista)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "Dados inválidos. Verifique as informações fornecidas"
                    401 -> "Não autorizado. Por favor, faça login novamente"
                    403 -> "Acesso negado. Você não tem permissão para esta ação"
                    409 -> "Este usuário já está cadastrado"
                    422 -> "Dados incompletos ou inválidos"
                    500 -> "Erro interno do servidor. Tente novamente mais tarde"
                    else -> "Erro no cadastro: ${response.code()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is UnknownHostException -> "Erro de conexão. Verifique sua internet"
                is SocketTimeoutException -> "Tempo de conexão esgotado. Tente novamente"
                else -> "Erro no cadastro: ${e.message}"
            }
            Result.failure(Exception(errorMessage))
        }
    }

    suspend fun getClinicas(): Result<List<ClinicResponse>> {
        return try {
            val response = apiServiceNoAuth.getClinicas()

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "Requisição inválida"
                    401 -> "Não autorizado. Por favor, faça login novamente"
                    403 -> {
                        try {
                            // Se receber 403, tenta novamente sem autenticação
                            val retrofitNoAuth = RetrofitConfig.getInstanceNoAuth(context)
                            val retryResponse = retrofitNoAuth.getClinicas()

                            if (retryResponse.isSuccessful && retryResponse.body() != null) {
                                return Result.success(retryResponse.body()!!)
                            } else {
                                "Erro ao buscar clínicas: ${retryResponse.code()}"
                            }
                        } catch (e: Exception) {
                            "Erro ao buscar clínicas sem autenticação: ${e.message}"
                        }
                    }
                    404 -> "Nenhuma clínica encontrada"
                    500 -> "Erro interno do servidor. Tente novamente mais tarde"
                    else -> "Erro ao buscar clínicas: ${response.code()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is UnknownHostException -> "Erro de conexão. Verifique sua internet"
                is SocketTimeoutException -> "Tempo de conexão esgotado. Tente novamente"
                else -> "Erro ao buscar clínicas: ${e.message}"
            }
            Result.failure(Exception(errorMessage))
        }
    }

    suspend fun getDentists(): Result<List<DentistResponse>> {
        return try {
            val response = apiService.getDentists(role = "DENTISTA")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "Requisição inválida"
                    401 -> "Não autorizado. Por favor, faça login novamente"
                    403 -> "Acesso negado. Você não tem permissão para esta ação"
                    404 -> "Nenhum dentista encontrado"
                    500 -> "Erro interno do servidor. Tente novamente mais tarde"
                    else -> handleApiError(response)
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception(handleException(e)))
        }
    }

    private fun handleApiError(response: retrofit2.Response<*>): String {
        val errorBody = response.errorBody()?.string()
        return when (response.code()) {
            400 -> "Requisição inválida"
            401 -> "Não autorizado. Por favor, faça login novamente"
            403 -> "Acesso negado. Você não tem permissão para esta ação"
            404 -> "Recurso não encontrado"
            409 -> "Conflito - Recurso já existe"
            422 -> "Dados inválidos"
            500 -> "Erro interno do servidor"
            else -> errorBody ?: "Erro desconhecido: ${response.code()}"
        }
    }

    private fun handleException(e: Exception): String {
        return when (e) {
            is UnknownHostException -> "Erro de conexão. Verifique sua internet"
            is SocketTimeoutException -> "Tempo de conexão esgotado. Tente novamente"
            else -> e.message ?: "Erro desconhecido"
        }
    }
}