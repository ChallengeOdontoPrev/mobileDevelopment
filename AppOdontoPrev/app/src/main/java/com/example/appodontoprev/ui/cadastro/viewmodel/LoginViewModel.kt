package com.example.appodontoprev.ui.login.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appodontoprev.data.model.response.LoginResponse
import com.example.appodontoprev.data.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = LoginRepository(application.applicationContext)

    private val _loginStatus = MutableLiveData<Result<LoginResponse>>()
    val loginStatus: LiveData<Result<LoginResponse>> = _loginStatus

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun realizarLogin(email: String, senha: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _loginStatus.value = repository.realizarLogin(email, senha)
            } finally {
                _isLoading.value = false
            }
        }
    }
}