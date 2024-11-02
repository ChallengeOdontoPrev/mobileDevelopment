package com.example.appodontoprev

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.appodontoprev.ui.login.viewmodel.LoginViewModel

class LoginDentistaActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_dentista)

        progressBar = findViewById(R.id.progressBar)

        val botaoVoltar = findViewById<Button>(R.id.botaoVoltar)
        val cadastrarDentistaTextView = findViewById<TextView>(R.id.cadastrarDentistaTextView)
        val recuperarSenhaButton = findViewById<TextView>(R.id.recuperarSenhaButton)
        val btnEntrar = findViewById<Button>(R.id.btnEntrar)
        val emailEditText = findViewById<EditText>(R.id.inputEmailMedi)
        val senhaEditText = findViewById<EditText>(R.id.inputSenhaMedi)

        setupObservers()

        botaoVoltar.setOnClickListener {
            val intent = Intent(this, MenuLoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        cadastrarDentistaTextView.setOnClickListener {
            val intent = Intent(this, CadastroDentistaActivity::class.java)
            startActivity(intent)
        }

        recuperarSenhaButton.setOnClickListener {
            val intent = Intent(this, AlterarSenhaActivity::class.java)
            intent.putExtra("origem", "dentista")
            startActivity(intent)
        }

        btnEntrar.setOnClickListener {
            val email = emailEditText.text.toString()
            val senha = senhaEditText.text.toString()

            if (validarCampos(email, senha)) {
                viewModel.realizarLogin(email, senha)
            }
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.loginStatus.observe(this) { result ->
            result.onSuccess { response ->
                response.token?.let { token ->
                    val sharedPref = getSharedPreferences("AppOdontoPrev", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("token", token)
                        putString("tipoUsuario", "dentista")
                        apply()
                    }
                }

                val intent = Intent(this, MenuPrincipalActivity::class.java)
                startActivity(intent)
                finish()
            }.onFailure { exception ->
                showAlert("Erro no login: ${exception.message}")
            }
        }
    }

    private fun validarCampos(email: String, senha: String): Boolean {
        if (email.isBlank() || senha.isBlank()) {
            showAlert("Preencha todos os campos")
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showAlert("Email inválido")
            return false
        }
        return true
    }

    private fun showAlert(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Erro")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}