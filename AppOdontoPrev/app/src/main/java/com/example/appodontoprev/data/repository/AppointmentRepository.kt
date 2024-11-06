package com.example.appodontoprev.data.repository

import AppointmentResponse
import android.content.Context
import com.example.appodontoprev.data.api.RetrofitConfig
import com.example.appodontoprev.data.model.request.AppointmentRequest
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class AppointmentRepository(private val context: Context) {
    private val apiService = RetrofitConfig.getInstance(context)

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
}