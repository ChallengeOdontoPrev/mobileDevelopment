package com.example.appodontoprev

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.appodontoprev.ui.agendamento.viewmodel.AgendamentoConsultaViewModel
import java.util.Calendar

class AgendamentoConsutaActivity : AppCompatActivity() {
    private val viewModel: AgendamentoConsultaViewModel by viewModels()

    // Views
    private lateinit var progressBar: ProgressBar
    private lateinit var btnVolAgenConsul: ImageButton
    private lateinit var btnAgenConsul: Button
    private lateinit var buscaPaciente: EditText
    private lateinit var nomePaciente: EditText
    private lateinit var rgPaciente: EditText
    private lateinit var dataNascimentoPaciente: EditText
    private lateinit var idOdontoPrevPacinete: EditText
    private lateinit var spinnerServicos: Spinner
    private lateinit var dataConsulta: EditText
    private lateinit var horarioConsulta: EditText
    private lateinit var spinnerDentistas: Spinner

    private var isPatientFromSearch = false
    private var currentPatientId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendamento_consulta)

        setupViews()
        setupObservers()
        setupListeners()
    }

    private fun setupViews() {
        // Inicialização das views
        progressBar = findViewById(R.id.progressBar)
        btnVolAgenConsul = findViewById(R.id.btnVolAgenConsul)
        btnAgenConsul = findViewById(R.id.btnAgenConsul)
        buscaPaciente = findViewById(R.id.buscaPaciente)
        nomePaciente = findViewById(R.id.nomePaciente)
        rgPaciente = findViewById(R.id.rgPaciente)
        dataNascimentoPaciente = findViewById(R.id.dataNascimentoPaciente)
        idOdontoPrevPacinete = findViewById(R.id.idOdontoPrevPacinete)
        spinnerServicos = findViewById(R.id.spinnerServicos)
        dataConsulta = findViewById(R.id.dataConsulta)
        horarioConsulta = findViewById(R.id.horarioConsulta)
        spinnerDentistas = findViewById(R.id.spinnerDentistas)

        // Configurar campos de data e hora como não focáveis
        dataConsulta.isFocusable = false
        horarioConsulta.isFocusable = false
        dataNascimentoPaciente.isFocusable = false

        // Configurar campos editáveis
        setupEditableFields(true)
    }

    private fun setupEditableFields(editable: Boolean) {
        nomePaciente.isEnabled = editable
        rgPaciente.isEnabled = editable
        dataNascimentoPaciente.isEnabled = editable
        idOdontoPrevPacinete.isEnabled = editable
    }

    private fun setupObservers() {
        // Observer para estado de loading
        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            setFieldsEnabled(!isLoading)
        }

        // Observer para dados do paciente
        viewModel.patientData.observe(this) { result ->
            result.onSuccess { patient ->
                currentPatientId = patient.id
                nomePaciente.setText(patient.name)
                rgPaciente.setText(patient.rg)
                dataNascimentoPaciente.setText(viewModel.formatDateForDisplay(patient.birthDate))
                idOdontoPrevPacinete.setText(patient.numCard.toString())
                isPatientFromSearch = true
                setupEditableFields(false)
            }.onFailure { exception ->
                showErrorDialog(exception.message ?: "Erro desconhecido")
                clearFields()
                setupEditableFields(true)
                isPatientFromSearch = false
                currentPatientId = null
            }
        }

        // Observer para criação de paciente
        viewModel.patientCreated.observe(this) { result ->
            result.onSuccess { patient ->
                currentPatientId = patient.id
                showSuccessMessage("Paciente cadastrado com sucesso")
                createAppointment(patient.id)
            }.onFailure { exception ->
                showErrorDialog(exception.message ?: "Erro ao criar paciente")
            }
        }

        // Observer para procedimentos
        viewModel.procedures.observe(this) { result ->
            result.onSuccess { _ ->
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    viewModel.getProcedureNames()
                ).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
                spinnerServicos.adapter = adapter
            }.onFailure { exception ->
                showErrorDialog("Erro ao carregar procedimentos: ${exception.message}")
            }
        }

        // Observer para dentistas
        viewModel.dentists.observe(this) { result ->
            result.onSuccess { _ ->
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    viewModel.getDentistNames()
                ).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
                spinnerDentistas.adapter = adapter
            }.onFailure { exception ->
                showErrorDialog("Erro ao carregar dentistas: ${exception.message}")
            }
        }

        // Observer para agendamento
        viewModel.appointmentCreated.observe(this) { result ->
            result.onSuccess {
                // Navegar para a tela de ConsultaAgendada
                val intent = Intent(this, ConsultaAgendadaActivity::class.java)
                startActivity(intent)
                finish()
            }.onFailure { exception ->
                showErrorDialog(exception.message ?: "Erro ao criar agendamento")
            }
        }
    }

    private fun setupListeners() {
        // Listener para botão voltar
        btnVolAgenConsul.setOnClickListener {
            finish()
        }

        // Listener para botão de agendar
        btnAgenConsul.setOnClickListener {
            handleAppointment()
        }

        // Listener para busca de paciente
        buscaPaciente.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableRight = 2
                if (event.rawX >= (buscaPaciente.right - buscaPaciente.compoundDrawables[drawableRight].bounds.width())) {
                    performSearch()
                    return@setOnTouchListener true
                }
            }
            false
        }

        // Listeners para campos de data
        dataNascimentoPaciente.setOnClickListener {
            if (!isPatientFromSearch) {
                showBirthDatePicker()
            }
        }

        dataConsulta.setOnClickListener {
            showAppointmentDatePicker()
        }

        horarioConsulta.setOnClickListener {
            showTimePicker()
        }
    }

    private fun handleAppointment() {
        if (!validateFields()) return

        if (!isPatientFromSearch) {
            // Criar novo paciente
            val name = nomePaciente.text.toString()
            val rg = rgPaciente.text.toString()
            val birthDate = dataNascimentoPaciente.text.toString()
            val numCard = idOdontoPrevPacinete.text.toString()

            if (viewModel.validatePatientData(name, rg, birthDate, numCard)) {
                viewModel.createPatient(
                    name = name,
                    rg = rg,
                    birthDate = birthDate,
                    numCard = numCard.toLong()
                )
            }
        } else {
            // Paciente já existe, criar agendamento direto
            currentPatientId?.let { createAppointment(it) }
                ?: showErrorDialog("Erro ao identificar paciente")
        }
    }

    private fun createAppointment(patientId: Long) {
        val procedureId = viewModel.getSelectedProcedureId(spinnerServicos.selectedItemPosition)
        val dentistId = viewModel.getSelectedDentistId(spinnerDentistas.selectedItemPosition)
        val date = dataConsulta.text.toString()
        val time = horarioConsulta.text.toString()

        if (procedureId != null && dentistId != null) {
            if (viewModel.validateAppointmentData(date, time)) {
                viewModel.createAppointment(
                    date = date,
                    time = time,
                    dentistId = dentistId,
                    patientId = patientId,
                    procedureId = procedureId
                )
            }
        } else {
            showErrorDialog("Erro ao obter dados do agendamento")
        }
    }

    private fun performSearch() {
        val rg = buscaPaciente.text.toString().trim()
        if (rg.isNotEmpty()) {
            viewModel.searchPatientByRg(rg)
        } else {
            showErrorDialog("Digite um RG para buscar")
        }
    }

    private fun showBirthDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, day ->
                val selectedDate = String.format(
                    "%02d/%02d/%04d",
                    day,
                    month + 1,
                    year
                )
                dataNascimentoPaciente.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.maxDate = System.currentTimeMillis()
            show()
        }
    }

    private fun showAppointmentDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, day ->
                val selectedDate = String.format(
                    "%02d/%02d/%04d",
                    day,
                    month + 1,
                    year
                )
                dataConsulta.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = System.currentTimeMillis()
            show()
        }
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                if (isValidBusinessHour(hourOfDay, minute)) {
                    val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                    horarioConsulta.setText(selectedTime)
                } else {
                    showErrorDialog("Selecione um horário entre 8:00 e 18:00")
                }
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun isValidBusinessHour(hour: Int, minute: Int): Boolean {
        return (hour in 8..17) || (hour == 18 && minute == 0)
    }

    private fun validateFields(): Boolean {
        return when {
            nomePaciente.text.isBlank() -> {
                showErrorDialog("Nome do paciente é obrigatório")
                false
            }
            rgPaciente.text.isBlank() -> {
                showErrorDialog("RG do paciente é obrigatório")
                false
            }
            dataNascimentoPaciente.text.isBlank() -> {
                showErrorDialog("Data de nascimento é obrigatória")
                false
            }
            idOdontoPrevPacinete.text.isBlank() -> {
                showErrorDialog("ID Odontoprev é obrigatório")
                false
            }
            dataConsulta.text.isBlank() -> {
                showErrorDialog("Data da consulta é obrigatória")
                false
            }
            horarioConsulta.text.isBlank() -> {
                showErrorDialog("Horário da consulta é obrigatório")
                false
            }
            spinnerServicos.selectedItemPosition == 0 -> {
                showErrorDialog("Selecione um procedimento")
                false
            }
            spinnerDentistas.selectedItemPosition == 0 -> {
                showErrorDialog("Selecione um dentista")
                false
            }
            else -> true
        }
    }

    private fun setFieldsEnabled(enabled: Boolean) {
        spinnerServicos.isEnabled = enabled
        dataConsulta.isEnabled = enabled
        horarioConsulta.isEnabled = enabled
        spinnerDentistas.isEnabled = enabled
        btnAgenConsul.isEnabled = enabled
        buscaPaciente.isEnabled = enabled
    }

    private fun clearFields() {
        nomePaciente.setText("")
        rgPaciente.setText("")
        dataNascimentoPaciente.setText("")
        idOdontoPrevPacinete.setText("")
        isPatientFromSearch = false
        currentPatientId = null
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Atenção")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showSuccessMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}