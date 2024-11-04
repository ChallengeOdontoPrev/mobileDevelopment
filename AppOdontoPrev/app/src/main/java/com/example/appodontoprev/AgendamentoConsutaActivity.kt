package com.example.appodontoprev

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
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

        // Configurar campos como não editáveis
        nomePaciente.isEnabled = false
        rgPaciente.isEnabled = false
        dataNascimentoPaciente.isEnabled = false
        idOdontoPrevPacinete.isEnabled = false

        // Configurar focusable=false para os campos de data e hora
        dataConsulta.isFocusable = false
        horarioConsulta.isFocusable = false
    }

    private fun setupObservers() {
        // Observer para o estado de loading
        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            // Desabilitar a interação com a UI durante o carregamento
            btnAgenConsul.isEnabled = !isLoading
            buscaPaciente.isEnabled = !isLoading
            spinnerServicos.isEnabled = !isLoading
            spinnerDentistas.isEnabled = !isLoading
            dataConsulta.isEnabled = !isLoading
            horarioConsulta.isEnabled = !isLoading
        }

        // Observer para os dados do paciente
        viewModel.patientData.observe(this) { result ->
            result.onSuccess { patient ->
                // Preencher campos com os dados do paciente
                nomePaciente.setText(patient.name)
                rgPaciente.setText(patient.rg)
                dataNascimentoPaciente.setText(formatDate(patient.birthDate))
                idOdontoPrevPacinete.setText(patient.numCard.toString())
            }.onFailure { exception ->
                // Mostrar erro e limpar campos
                showErrorDialog(exception.message ?: "Erro desconhecido")
                clearFields()
            }
        }
    }

    private fun setupListeners() {
        // Listener para o botão voltar
        btnVolAgenConsul.setOnClickListener {
            val intent = Intent(this, ConsultasActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Listener para o botão de agendamento
        btnAgenConsul.setOnClickListener {
            if (validateFields()) {
                showAgendamentoDialog()
            } else {
                showErrorDialog("Preencha todos os campos obrigatórios")
            }
        }

        // Listener para o ícone de busca no EditText
        buscaPaciente.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableRight = 2
                if (event.rawX >= (buscaPaciente.right - buscaPaciente.compoundDrawables[drawableRight].bounds.width())) {
                    performSearch()
                    return@setOnTouchListener true
                }
            }
            false
        }

        // Listener para o campo de data
        dataConsulta.setOnClickListener {
            showDatePickerDialog()
        }

        // Listener para o campo de horário
        horarioConsulta.setOnClickListener {
            showTimePickerDialog()
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

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = String.format(
                    "%02d/%02d/%04d",
                    dayOfMonth,
                    month + 1,
                    year
                )
                dataConsulta.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Definir data mínima como hoje
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()

        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()

        TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                // Verificar se está dentro do horário comercial (8h às 18h)
                if (hourOfDay in 8..17 || (hourOfDay == 18 && minute == 0)) {
                    val selectedTime = String.format(
                        "%02d:%02d",
                        hourOfDay,
                        minute
                    )
                    horarioConsulta.setText(selectedTime)
                } else {
                    showErrorDialog("Por favor, selecione um horário entre 8:00 e 18:00")
                }
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun showAgendamentoDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Agendamento")
            .setMessage("Deseja confirmar o agendamento desta consulta?")
            .setPositiveButton("Confirmar") { _, _ ->
                // TODO: Implementar a chamada à API para salvar o agendamento
                Toast.makeText(this, "Em breve: agendamento de consultas", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun validateFields(): Boolean {
        return buscaPaciente.text.isNotEmpty() &&
                nomePaciente.text.isNotEmpty() &&
                dataConsulta.text.isNotEmpty() &&
                horarioConsulta.text.isNotEmpty() &&
                spinnerServicos.selectedItemPosition != 0 &&
                spinnerDentistas.selectedItemPosition != 0
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
}