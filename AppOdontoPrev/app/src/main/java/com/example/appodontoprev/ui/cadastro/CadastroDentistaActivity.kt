package com.example.appodontoprev

import CadastroDentistaViewModel
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.appodontoprev.data.model.response.ClinicResponse
import java.util.Calendar

class CadastroDentistaActivity : AppCompatActivity() {
    private val viewModel: CadastroDentistaViewModel by viewModels()
    private lateinit var spinner: Spinner
    private lateinit var progressBar: ProgressBar
    private lateinit var clinicas: List<ClinicResponse>
    private lateinit var editTextDataNascimento: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_dentista)

        setupViews()
        setupObservers()
        setupListeners()
        setupDatePicker()
    }

    private fun setupViews() {
        spinner = findViewById(R.id.spinner)
        progressBar = findViewById(R.id.progressBar)
        editTextDataNascimento = findViewById(R.id.editTextDataNascimento)

        findViewById<TextView>(R.id.viewVolLogDent).setOnClickListener {
            startActivity(Intent(this, LoginDentistaActivity::class.java))
            finish()
        }

        findViewById<Button>(R.id.botVolLoginDent).setOnClickListener {
            startActivity(Intent(this, LoginDentistaActivity::class.java))
            finish()
        }
    }

    private fun setupDatePicker() {
        editTextDataNascimento.setOnClickListener {
            val calendar = Calendar.getInstance()

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    // Formato dd/MM/yyyy
                    val formattedDate = String.format(
                        "%02d/%02d/%04d",
                        dayOfMonth,
                        month + 1,
                        year
                    )
                    editTextDataNascimento.setText(formattedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            // Define data máxima (hoje)
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

            // Define data mínima (100 anos atrás)
            calendar.add(Calendar.YEAR, -100)
            datePickerDialog.datePicker.minDate = calendar.timeInMillis

            datePickerDialog.show()
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.clinicas.observe(this) { result ->
            result.onSuccess { clinicasList ->
                clinicas = clinicasList
                val clinicasNomes = clinicasList.map { it.name }
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, clinicasNomes)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }.onFailure {
                showError("Erro ao carregar clínicas: ${it.message}")
            }
        }

        viewModel.cadastroStatus.observe(this) { result ->
            result.onSuccess {
                val intent = Intent(this, CadastroConcluidoActivity::class.java)
                intent.putExtra("tipo_usuario", "dentista")
                startActivity(intent)
                finish()
            }.onFailure {
                showError("Erro no cadastro: ${it.message}")
            }
        }
    }

    private fun setupListeners() {
        findViewById<Button>(R.id.btnCadastrarDent).setOnClickListener {
            realizarCadastro()
        }
    }

    private fun realizarCadastro() {
        val email = findViewById<EditText>(R.id.editTextTextEmailAddress).text.toString()
        val senha = findViewById<EditText>(R.id.editTextTextPassword).text.toString()
        val nome = findViewById<EditText>(R.id.editTextText9).text.toString()
        val rg = findViewById<EditText>(R.id.editTextRG).text.toString()
        val dataNascimento = editTextDataNascimento.text.toString()
        val cro = findViewById<EditText>(R.id.editTextText2).text.toString()
        val spinnerPosition = spinner.selectedItemPosition

        if (validarCampos(email, senha, nome, rg, cro, dataNascimento) && spinnerPosition >= 0) {
            val clinicId = clinicas[spinnerPosition].id
            // Converter data para formato yyyy-MM-dd antes de enviar para a API
            val dataFormatadaAPI = convertDateFormatToAPI(dataNascimento)
            viewModel.cadastrarDentista(
                email = email,
                senha = senha,
                nome = nome,
                rg = rg,
                dataNascimento = dataFormatadaAPI,
                cro = cro,
                clinicId = clinicId
            )
        }
    }

    private fun convertDateFormatToAPI(date: String): String {
        // Converte de dd/MM/yyyy para yyyy-MM-dd
        val parts = date.split("/")
        return "${parts[2]}-${parts[1]}-${parts[0]}"
    }

    private fun validarCampos(email: String, senha: String, nome: String, rg: String, cro: String, dataNascimento: String): Boolean {
        if (email.isBlank() || senha.isBlank() || nome.isBlank() || rg.isBlank() || cro.isBlank() || dataNascimento.isBlank()) {
            showError("Todos os campos são obrigatórios")
            return false
        }

        if (!isValidDate(dataNascimento)) {
            showError("Data de nascimento deve estar no formato DD/MM/AAAA")
            return false
        }

        return true
    }

    private fun isValidDate(date: String): Boolean {
        return try {
            val pattern = "\\d{2}/\\d{2}/\\d{4}".toRegex()
            if (!date.matches(pattern)) {
                return false
            }

            val parts = date.split("/")
            val day = parts[0].toInt()
            val month = parts[1].toInt()
            val year = parts[2].toInt()

            if (year < 1900 || year > 2100) return false
            if (month < 1 || month > 12) return false
            if (day < 1 || day > 31) return false

            true
        } catch (e: Exception) {
            false
        }
    }

    private fun showError(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Erro")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}