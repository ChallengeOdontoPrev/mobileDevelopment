package com.example.appodontoprev

import CadastroAtendenteViewModel
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

class CadastroAtendenteActivity : AppCompatActivity() {
    private val viewModel: CadastroAtendenteViewModel by viewModels()
    private lateinit var spinner: Spinner
    private lateinit var progressBar: ProgressBar
    private lateinit var clinicas: List<ClinicResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cadastro_atendente_activity)

        setupViews()
        setupObservers()
        setupListeners()
    }

    private fun setupViews() {
        spinner = findViewById(R.id.spinner)
        progressBar = findViewById(R.id.progressBar)

        findViewById<TextView>(R.id.botaoVoltarLogin).setOnClickListener {
            startActivity(Intent(this, LoginAtendenteActivity::class.java))
            finish()
        }

        findViewById<Button>(R.id.botaoVoltarCadastro).setOnClickListener {
            startActivity(Intent(this, LoginAtendenteActivity::class.java))
            finish()
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
                showSuccess("Cadastro realizado com sucesso!")
                startActivity(Intent(this, LoginAtendenteActivity::class.java))
                finish()
            }.onFailure {
                showError("Erro no cadastro: ${it.message}")
            }
        }
    }

    private fun setupListeners() {
        findViewById<Button>(R.id.btnCadastrarAten).setOnClickListener {
            realizarCadastro()
        }
    }

    private fun realizarCadastro() {
        val email = findViewById<EditText>(R.id.editTextTextEmailAddress).text.toString()
        val senha = findViewById<EditText>(R.id.editTextTextPassword).text.toString()
        val nome = findViewById<EditText>(R.id.editTextText9).text.toString()
        val spinnerPosition = spinner.selectedItemPosition

        if (validarCampos(email, senha, nome) && spinnerPosition >= 0) {
            val clinicId = clinicas[spinnerPosition].id
            viewModel.cadastrarAtendente(
                email = email,
                senha = senha,
                nome = nome,
                rg = "12345678", // Você precisa adicionar um campo RG no layout
                dataNascimento = "2000-01-01", // Você precisa adicionar um campo data no layout
                clinicId = clinicId
            )
        }
    }

    private fun validarCampos(email: String, senha: String, nome: String): Boolean {
        if (email.isBlank() || senha.isBlank() || nome.isBlank()) {
            showError("Todos os campos são obrigatórios")
            return false
        }
        return true
    }

    private fun showError(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Erro")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showSuccess(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Sucesso")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}