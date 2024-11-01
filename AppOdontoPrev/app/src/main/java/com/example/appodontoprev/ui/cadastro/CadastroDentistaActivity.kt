package com.example.appodontoprev

import CadastroDentistaViewModel
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

class CadastroDentistaActivity : AppCompatActivity() {
    private val viewModel: CadastroDentistaViewModel by viewModels()
    private lateinit var spinner: Spinner
    private lateinit var progressBar: ProgressBar
    private lateinit var clinicas: List<ClinicResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_dentista)

        setupViews()
        setupObservers()
        setupListeners()
    }

    private fun setupViews() {
        spinner = findViewById(R.id.spinner)
        progressBar = findViewById(R.id.progressBar)

        findViewById<TextView>(R.id.viewVolLogDent).setOnClickListener {
            startActivity(Intent(this, LoginDentistaActivity::class.java))
            finish()
        }

        findViewById<Button>(R.id.botVolLoginDent).setOnClickListener {
            startActivity(Intent(this, LoginDentistaActivity::class.java))
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
                val intent = Intent(this, CadastroConcluidoActivity::class.java)
                intent.putExtra("tipo_usuario", "dentista") // Adicionando informação do tipo
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
        val cro = findViewById<EditText>(R.id.editTextText2).text.toString()
        val spinnerPosition = spinner.selectedItemPosition

        if (validarCampos(email, senha, nome, rg, cro) && spinnerPosition >= 0) {
            val clinicId = clinicas[spinnerPosition].id
            viewModel.cadastrarDentista(
                email = email,
                senha = senha,
                nome = nome,
                rg = rg,
                dataNascimento = "2000-01-01", // Aqui você deve adicionar um campo para data de nascimento no layout
                cro = cro,
                clinicId = clinicId
            )
        }
    }

    private fun validarCampos(email: String, senha: String, nome: String, rg: String, cro: String): Boolean {
        if (email.isBlank() || senha.isBlank() || nome.isBlank() || rg.isBlank() || cro.isBlank()) {
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
}