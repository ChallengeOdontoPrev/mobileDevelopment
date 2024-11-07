package com.example.appodontoprev.data.repository

import AppointmentResponse
import android.content.Context
import com.example.appodontoprev.data.api.RetrofitConfig
import com.example.appodontoprev.data.model.request.AppointmentRequest
import com.example.appodontoprev.data.model.response.AppointmentWithPatientName
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class AppointmentRepository(private val context: Context) {
    private val apiService = RetrofitConfig.getInstance(context)

    // Criar nova consulta
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

    // Buscar todas as consultas com nomes dos pacientes
    suspend fun getAppointmentsWithPatientNames(): Result<List<AppointmentWithPatientName>> {
        return try {
            val appointmentsResult = getAppointments()

            if (appointmentsResult.isSuccess) {
                val appointments = appointmentsResult.getOrNull() ?: emptyList()
                val appointmentsWithNames = appointments.map { appointment ->
                    // Por enquanto, vamos apenas mostrar um identificador do paciente
                    // até implementarmos uma solução para buscar os dados do paciente
                    AppointmentWithPatientName(
                        id = appointment.id,
                        dateAppointment = appointment.dateAppointment,
                        timeAppointment = appointment.timeAppointment,
                        patientName = "ID do Paciente: ${appointment.patientId}",
                        dentistId = appointment.dentistId,
                        patientId = appointment.patientId,
                        procedureTypeId = appointment.procedureTypeId
                    )
                }
                Result.success(appointmentsWithNames)
            } else {
                Result.failure(appointmentsResult.exceptionOrNull() ?: Exception("Erro desconhecido"))
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

    // Buscar todas as consultas
    private suspend fun getAppointments(): Result<List<AppointmentResponse>> {
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

    // Deletar consulta
    suspend fun deleteAppointment(id: Long): Result<Unit> {
        return try {
            val response = apiService.deleteAppointment(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMessage = when (response.code()) {
                    401 -> "Não autorizado. Faça login novamente"
                    403 -> "Acesso negado"
                    404 -> "Consulta não encontrada"
                    500 -> "Erro interno do servidor"
                    else -> "Erro ao deletar consulta: ${response.code()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is UnknownHostException -> "Erro de conexão. Verifique sua internet"
                is SocketTimeoutException -> "Tempo de conexão esgotado. Tente novamente"
                else -> "Erro ao deletar consulta: ${e.message}"
            }
            Result.failure(Exception(errorMessage))
        }
    }

    // Função auxiliar para tratar erros comuns da API
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

    // Função auxiliar para tratar exceções comuns
    private fun handleException(e: Exception): String {
        return when (e) {
            is UnknownHostException -> "Erro de conexão. Verifique sua internet"
            is SocketTimeoutException -> "Tempo de conexão esgotado. Tente novamente"
            else -> e.message ?: "Erro desconhecido"
        }
    }
}