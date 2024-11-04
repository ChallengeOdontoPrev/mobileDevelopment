package com.example.appodontoprev.data.repository

import android.content.Context
import com.example.appodontoprev.data.api.RetrofitConfig
import com.example.appodontoprev.data.model.request.LoginRequest
import com.example.appodontoprev.data.model.response.LoginResponse

class LoginRepository(private val context: Context) {
    private val apiService = RetrofitConfig.getInstance(context)

    suspend fun realizarLogin(email: String, senha: String): Result<LoginResponse> {
        return try {
            val request = LoginRequest(email, senha)
            val response = apiService.realizarLogin(request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Erro no login: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}