package com.example.appodontoprev.ui.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appodontoprev.data.model.response.LoginResponse
import com.example.appodontoprev.data.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val repository = LoginRepository()

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