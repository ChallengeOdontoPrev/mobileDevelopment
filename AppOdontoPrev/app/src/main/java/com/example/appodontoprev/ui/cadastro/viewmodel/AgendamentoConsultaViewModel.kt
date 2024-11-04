package com.example.appodontoprev.ui.agendamento.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.appodontoprev.data.model.response.PatientResponse
import com.example.appodontoprev.data.repository.PatientRepository
import kotlinx.coroutines.launch

class AgendamentoConsultaViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PatientRepository(application.applicationContext)

    private val _patientData = MutableLiveData<Result<PatientResponse>>()
    val patientData: LiveData<Result<PatientResponse>> = _patientData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun searchPatientByRg(rg: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _patientData.value = repository.getPatientByRg(rg)
            } finally {
                _isLoading.value = false
            }
        }
    }
}