package com.example.appodontoprev.data.repository

import android.content.Context
import com.example.appodontoprev.data.api.RetrofitConfig
import com.example.appodontoprev.data.model.request.PatientRequest
import com.example.appodontoprev.data.model.response.PatientResponse
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class PatientRepository(private val context: Context) {
    private val apiService = RetrofitConfig.getInstance(context)

    // Buscar paciente por RG
    suspend fun getPatientByRg(rg: String): Result<PatientResponse> {
        return try {
            val response = apiService.getPatientByRg(rg)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMessage = when (response.code()) {
                    404 -> "Paciente não encontrado"
                    400 -> "Requisição inválida"
                    401 -> "Não autorizado. Faça login novamente"
                    403 -> "Acesso negado. Você não tem permissão para esta ação"
                    500 -> "Erro interno do servidor"
                    else -> handleApiError(response)
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception(handleException(e)))
        }
    }

    // Criar novo paciente
    suspend fun createPatient(name: String, rg: String, birthDate: String, numCard: Long): Result<PatientResponse> {
        return try {
            val request = PatientRequest(
                name = name,
                rg = rg,
                birthDate = birthDate,
                numCard = numCard
            )

            val response = apiService.createPatient(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "Dados do paciente inválidos"
                    401 -> "Não autorizado. Faça login novamente"
                    403 -> "Acesso negado. Você não tem permissão para esta ação"
                    409 -> "Já existe um paciente com este RG"
                    422 -> "Dados incompletos ou inválidos"
                    500 -> "Erro interno do servidor"
                    else -> handleApiError(response)
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception(handleException(e)))
        }
    }

    // Método alternativo que recebe o objeto PatientRequest diretamente
    suspend fun createPatient(request: PatientRequest): Result<PatientResponse> {
        return try {
            val response = apiService.createPatient(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "Dados do paciente inválidos"
                    401 -> "Não autorizado. Faça login novamente"
                    403 -> "Acesso negado. Você não tem permissão para esta ação"
                    409 -> "Já existe um paciente com este RG"
                    422 -> "Dados incompletos ou inválidos"
                    500 -> "Erro interno do servidor"
                    else -> handleApiError(response)
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception(handleException(e)))
        }
    }

    // Função auxiliar para tratar erros da API
    private fun handleApiError(response: retrofit2.Response<*>): String {
        val errorBody = response.errorBody()?.string()
        return when (response.code()) {
            400 -> "Requisição inválida"
            401 -> "Não autorizado. Faça login novamente"
            403 -> "Acesso negado. Você não tem permissão para esta ação"
            404 -> "Recurso não encontrado"
            409 -> "Conflito - Recurso já existe"
            422 -> "Dados inválidos"
            500 -> "Erro interno do servidor"
            else -> errorBody ?: "Erro desconhecido: ${response.code()}"
        }
    }

    // Função auxiliar para tratar exceções comuns
    private fun handleException(e: Exception): String {
        return when (e) {
            is UnknownHostException -> "Erro de conexão. Verifique sua internet"
            is SocketTimeoutException -> "Tempo de conexão esgotado. Tente novamente"
            else -> e.message ?: "Erro desconhecido"
        }
    }

    // Função auxiliar para validar dados do paciente
    private fun validatePatientData(request: PatientRequest): Boolean {
        return request.name.isNotBlank() &&
                request.rg.isNotBlank() &&
                request.birthDate.isNotBlank() &&
                request.numCard > 0
    }

    // Função auxiliar para verificar se a data está no formato correto (yyyy-MM-dd)
    private fun isValidDateFormat(date: String): Boolean {
        return try {
            val pattern = "\\d{4}-\\d{2}-\\d{2}".toRegex()
            if (!date.matches(pattern)) {
                return false
            }

            val parts = date.split("-")
            val year = parts[0].toInt()
            val month = parts[1].toInt()
            val day = parts[2].toInt()

            if (year < 1900 || year > 2100) return false
            if (month < 1 || month > 12) return false
            if (day < 1 || day > 31) return false

            true
        } catch (e: Exception) {
            false
        }
    }
}