package com.example.appodontoprev.ui.agendamento.viewmodel

import DentistResponse
import ProcedureRepository
import ProcedureResponse
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.appodontoprev.data.model.request.PatientRequest
import com.example.appodontoprev.data.model.response.PatientResponse
import com.example.appodontoprev.data.repository.DentistRepository
import com.example.appodontoprev.data.repository.PatientRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AgendamentoConsultaViewModel(application: Application) : AndroidViewModel(application) {
    private val patientRepository = PatientRepository(application.applicationContext)
    private val procedureRepository = ProcedureRepository(application.applicationContext)
    private val dentistRepository = DentistRepository(application.applicationContext)

    // LiveData para os dados do paciente
    private val _patientData = MutableLiveData<Result<PatientResponse>>()
    val patientData: LiveData<Result<PatientResponse>> = _patientData

    // LiveData para criação de paciente
    private val _patientCreated = MutableLiveData<Result<PatientResponse>>()
    val patientCreated: LiveData<Result<PatientResponse>> = _patientCreated

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

    // Criar novo paciente
    fun createPatient(name: String, rg: String, birthDate: String, numCard: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val formattedDate = formatDateForApi(birthDate)
                val request = PatientRequest(
                    name = name,
                    rg = rg,
                    birthDate = formattedDate,
                    numCard = numCard
                )
                _patientCreated.value = patientRepository.createPatient(request)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Erro ao criar paciente"
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
                val result = dentistRepository.getDentists()
                _dentists.value = result
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Erro ao carregar dentistas"
                _dentists.value = Result.failure(e)
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

    // Obter lista de nomes dos dentistas para o spinner
    fun getDentistNames(): List<String> {
        val defaultItem = listOf("Selecione um dentista")
        return defaultItem + (_dentists.value?.getOrNull()?.map { it.name } ?: emptyList())
    }

    // Obter lista de nomes dos procedimentos para o spinner
    fun getProcedureNames(): List<String> {
        val defaultItem = listOf("Selecione um procedimento")
        return defaultItem + (_procedures.value?.getOrNull()?.map { it.name } ?: emptyList())
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
        return _procedures.value?.isFailure == true || _dentists.value?.isFailure == true
    }

    // Formatar data para a API (converter de dd/MM/yyyy para yyyy-MM-dd)
    private fun formatDateForApi(date: String): String {
        return try {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val parsedDate = inputFormat.parse(date)
            outputFormat.format(parsedDate!!)
        } catch (e: Exception) {
            throw IllegalArgumentException("Formato de data inválido. Use dd/MM/yyyy")
        }
    }

    // Formatar data para exibição (converter de yyyy-MM-dd para dd/MM/yyyy)
    fun formatDateForDisplay(date: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val parsedDate = inputFormat.parse(date)
            outputFormat.format(parsedDate!!)
        } catch (e: Exception) {
            date
        }
    }

    // Validar data
    fun isValidDate(date: String): Boolean {
        return try {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(date)
            true
        } catch (e: Exception) {
            false
        }
    }

    // Validar campos do paciente
    fun validatePatientData(name: String, rg: String, birthDate: String, numCard: String): Boolean {
        if (name.isBlank() || rg.isBlank() || birthDate.isBlank() || numCard.isBlank()) {
            _errorMessage.value = "Todos os campos são obrigatórios"
            return false
        }

        if (!isValidDate(birthDate)) {
            _errorMessage.value = "Data de nascimento inválida"
            return false
        }

        try {
            numCard.toLong()
        } catch (e: NumberFormatException) {
            _errorMessage.value = "Número do cartão inválido"
            return false
        }

        return true
    }
}