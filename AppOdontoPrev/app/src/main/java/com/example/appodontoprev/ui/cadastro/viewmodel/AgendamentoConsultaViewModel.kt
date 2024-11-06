package com.example.appodontoprev.ui.agendamento.viewmodel

import DentistRepository
import DentistResponse
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
    private val dentistRepository = DentistRepository(application.applicationContext)

    // LiveData para os dados do paciente
    private val _patientData = MutableLiveData<Result<PatientResponse>>()
    val patientData: LiveData<Result<PatientResponse>> = _patientData

    // LiveData para os procedimentos
    private val _procedures = MutableLiveData<Result<List<ProcedureResponse>>>()
    val procedures: LiveData<Result<List<ProcedureResponse>>> = _procedures

    // LiveData para os dentistas
    private val _dentists = MutableLiveData<Result<List<DentistResponse>>>()
    val dentists: LiveData<Result<List<DentistResponse>>> = _dentists

    // LiveData para estado de carregamento
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData para mensagens de erro
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    // LiveData para mensagens de sucesso
    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> = _successMessage

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        loadProcedures()
        loadDentists()
    }

    // Buscar paciente por RG
    fun searchPatientByRg(rg: String) {
        if (rg.isBlank()) {
            _errorMessage.value = "Digite um RG para buscar"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                _patientData.value = patientRepository.getPatientByRg(rg)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Erro ao buscar paciente"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Carregar procedimentos
    private fun loadProcedures() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _procedures.value = procedureRepository.getProcedures()
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Erro ao carregar procedimentos"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Carregar dentistas
    private fun loadDentists() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _dentists.value = dentistRepository.getDentists()
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Erro ao carregar dentistas"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Recarregar dados
    fun refreshData() {
        loadInitialData()
    }

    // Limpar mensagens
    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }

    // Validar seleção de procedimento
    fun isValidProcedureSelected(position: Int): Boolean {
        return position > 0
    }

    // Validar seleção de dentista
    fun isValidDentistSelected(position: Int): Boolean {
        return position > 0
    }

    // Obter nome do procedimento selecionado
    fun getSelectedProcedureName(position: Int): String? {
        return _procedures.value?.getOrNull()?.getOrNull(position - 1)?.name
    }

    // Obter nome do dentista selecionado
    fun getSelectedDentistName(position: Int): String? {
        return _dentists.value?.getOrNull()?.getOrNull(position - 1)?.name
    }

    // Obter ID do procedimento selecionado
    fun getSelectedProcedureId(position: Int): Long? {
        return _procedures.value?.getOrNull()?.getOrNull(position - 1)?.id
    }

    // Obter ID do dentista selecionado
    fun getSelectedDentistId(position: Int): Long? {
        return _dentists.value?.getOrNull()?.getOrNull(position - 1)?.id
    }

    // Verificar se há dados carregados
    fun hasLoadedData(): Boolean {
        return _procedures.value != null && _dentists.value != null
    }

    // Verificar se há erros
    fun hasErrors(): Boolean {
        return _procedures.value?.isFailure == true ||
                _dentists.value?.isFailure == true
    }
}