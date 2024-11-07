package com.example.appodontoprev.data.repository

import AppointmentListResponse
import AppointmentResponse
import android.content.Context
import com.example.appodontoprev.data.api.RetrofitConfig
import com.example.appodontoprev.data.model.request.AppointmentRequest
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class AppointmentRepository(private val context: Context) {
    private val apiService = RetrofitConfig.getInstance(context)

    // Método existente - não modificado
    suspend fun createAppointment(request: AppointmentRequest): Result<AppointmentResponse> {
        return try {
            val response = apiService.createAppointment(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "Dados da consulta inválidos"
                    401 -> "Não autorizado. Faça login novamente"
                    403 -> "Acesso negado"
                    409 -> "Já existe uma consulta agendada neste horário"
                    422 -> "Dados incompletos ou inválidos"
                    500 -> "Erro interno do servidor"
                    else -> "Erro ao criar agendamento: ${response.code()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is UnknownHostException -> "Erro de conexão. Verifique sua internet"
                is SocketTimeoutException -> "Tempo de conexão esgotado. Tente novamente"
                else -> "Erro ao criar agendamento: ${e.message}"
            }
            Result.failure(Exception(errorMessage))
        }
    }

    // Novo método para listar consultas
    suspend fun getAppointments(): Result<List<AppointmentListResponse>> {
        return try {
            val response = apiService.getAppointments()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMessage = when (response.code()) {
                    401 -> "Não autorizado. Faça login novamente"
                    403 -> "Acesso negado"
                    404 -> "Nenhuma consulta encontrada"
                    500 -> "Erro interno do servidor"
                    else -> "Erro ao buscar consultas: ${response.code()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is UnknownHostException -> "Erro de conexão. Verifique sua internet"
                is SocketTimeoutException -> "Tempo de conexão esgotado. Tente novamente"
                else -> "Erro ao buscar consultas: ${e.message}"
            }
            Result.failure(Exception(errorMessage))
        }
    }



    // Métodos auxiliares existentes - não modificados
    private fun handleApiError(response: retrofit2.Response<*>): String {
        return when (response.code()) {
            400 -> "Requisição inválida"
            401 -> "Não autorizado. Faça login novamente"
            403 -> "Acesso negado"
            404 -> "Recurso não encontrado"
            409 -> "Conflito - Recurso já existe"
            422 -> "Dados inválidos"
            500 -> "Erro interno do servidor"
            else -> "Erro desconhecido: ${response.code()}"
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