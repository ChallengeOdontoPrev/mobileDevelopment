package com.example.appodontoprev.data.repository

import android.content.Context
import com.example.appodontoprev.data.api.RetrofitConfig
import com.example.appodontoprev.data.model.request.LoginRequest
import com.example.appodontoprev.data.model.response.LoginResponse

class LoginRepository(private val context: Context) {
    private val apiService = RetrofitConfig.getInstanceNoAuth(context) // Usar instância sem auth

    suspend fun realizarLogin(email: String, senha: String): Result<LoginResponse> {
        return try {
            val request = LoginRequest(email, senha)
            val response = apiService.realizarLogin(request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                when (response.code()) {
                    400 -> Result.failure(Exception("Dados inválidos"))
                    401 -> Result.failure(Exception("Email ou senha incorretos"))
                    403 -> Result.failure(Exception("Acesso não autorizado"))
                    500 -> Result.failure(Exception("Erro interno do servidor"))
                    else -> Result.failure(Exception("Erro no login: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: ${e.message}"))
        }
    }
}