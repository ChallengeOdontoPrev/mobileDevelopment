package com.example.appodontoprev.data.api

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Não adiciona token para o endpoint de clínicas durante o cadastro
        if (originalRequest.url.encodedPath.endsWith("/clinics")) {
            return chain.proceed(originalRequest)
        }

        val sharedPref = context.getSharedPreferences("AppOdontoPrev", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)

        return if (token != null) {
            val newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(originalRequest)
        }
    }
}