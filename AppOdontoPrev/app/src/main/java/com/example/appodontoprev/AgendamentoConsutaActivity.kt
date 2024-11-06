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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
                nomePaciente.setText(patient.name)
                rgPaciente.setText(patient.rg)
                dataNascimentoPaciente.setText(formatDate(patient.birthDate))
                idOdontoPrevPacinete.setText(patient.numCard.toString())
            }.onFailure { exception ->
                showErrorDialog(exception.message ?: "Erro desconhecido")
                clearFields()
            }
        }

        // Observer para procedimentos
        viewModel.procedures.observe(this) { result ->
            result.onSuccess { procedures ->
                val procedureNames = listOf("Selecione um procedimento") + procedures.map { it.name }
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    procedureNames
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
            result.onSuccess { dentists ->
                val dentistNames = listOf("Selecione um dentista") + dentists.map { it.name }
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    dentistNames
                ).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
                spinnerDentistas.adapter = adapter
            }.onFailure { exception ->
                showErrorDialog("Erro ao carregar dentistas: ${exception.message}")
            }
        }

        // Observer para mensagens de erro
        viewModel.errorMessage.observe(this) { message ->
            message?.let {
                showErrorDialog(it)
                viewModel.clearMessages()
            }
        }

        // Observer para mensagens de sucesso
        viewModel.successMessage.observe(this) { message ->
            message?.let {
                showSuccessMessage(it)
                viewModel.clearMessages()
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
            if (validateFields()) {
                proceedWithAppointment()
            }
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

        // Listeners para campos de data e hora
        dataNascimentoPaciente.setOnClickListener {
            showBirthDatePicker()
        }

        dataConsulta.setOnClickListener {
            showAppointmentDatePicker()
        }

        horarioConsulta.setOnClickListener {
            showTimePicker()
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

    private fun proceedWithAppointment() {
        // Obter dados selecionados
        val procedureId = viewModel.getSelectedProcedureId(spinnerServicos.selectedItemPosition)
        val dentistId = viewModel.getSelectedDentistId(spinnerDentistas.selectedItemPosition)
        val date = dataConsulta.text.toString()
        val time = horarioConsulta.text.toString()

        if (procedureId != null && dentistId != null) {
            // TODO: Implementar lógica de agendamento
            AlertDialog.Builder(this)
                .setTitle("Agendamento")
                .setMessage("Agendamento de consulta em breve.")
                .setPositiveButton("OK", null)
                .show()
        } else {
            showErrorDialog("Erro ao obter dados do agendamento")
        }
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
        nomePaciente.isEnabled = enabled
        rgPaciente.isEnabled = enabled
        dataNascimentoPaciente.isEnabled = enabled
        idOdontoPrevPacinete.isEnabled = enabled
        spinnerServicos.isEnabled = enabled
        dataConsulta.isEnabled = enabled
        horarioConsulta.isEnabled = enabled
        spinnerDentistas.isEnabled = enabled
        btnAgenConsul.isEnabled = enabled
    }

    private fun formatDate(dateStr: String): String {
        return try {
            val originalFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val targetFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = originalFormat.parse(dateStr)
            targetFormat.format(date!!)
        } catch (e: Exception) {
            dateStr
        }
    }

    private fun clearFields() {
        nomePaciente.setText("")
        rgPaciente.setText("")
        dataNascimentoPaciente.setText("")
        idOdontoPrevPacinete.setText("")
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