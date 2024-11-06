package com.example.appodontoprev.ui.agendamento.viewmodel

import ProcedureRepository
import ProcedureResponse
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.appodontoprev.data.model.response.PatientResponse
import com.example.appodontoprev.data.repository.PatientRepository
import kotlinx.coroutines.launch

class AgendamentoConsultaViewModel(application: Application) : AndroidViewModel(application) {
    private val patientRepository = PatientRepository(application.applicationContext)
    private val procedureRepository = ProcedureRepository(application.applicationContext)

    private val _patientData = MutableLiveData<Result<PatientResponse>>()
    val patientData: LiveData<Result<PatientResponse>> = _patientData

    private val _procedures = MutableLiveData<Result<List<ProcedureResponse>>>()
    val procedures: LiveData<Result<List<ProcedureResponse>>> = _procedures

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadProcedures()
    }

    fun searchPatientByRg(rg: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _patientData.value = patientRepository.getPatientByRg(rg)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadProcedures() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _procedures.value = procedureRepository.getProcedures()
            } finally {
                _isLoading.value = false
            }
        }
    }
}