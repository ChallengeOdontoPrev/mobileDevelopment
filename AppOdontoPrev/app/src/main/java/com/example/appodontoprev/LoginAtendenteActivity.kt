package com.example.appodontoprev

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.util.Patterns

class LoginAtendenteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_atendente)

        // Find the UI elements
        val botaoVoltar = findViewById<Button>(R.id.botaoVoltar)
        val cadastrarAtendenteTextView = findViewById<TextView>(R.id.cadastrarAtendente)
        val recuperarSenhaButton = findViewById<TextView>(R.id.recuperarSenhaButton)
        val btnEntrar = findViewById<Button>(R.id.btnEntrar)

        val emailEditText = findViewById<EditText>(R.id.inputEmailAten)
        val senhaEditText = findViewById<EditText>(R.id.inputSenhaAten)

        // Navigate back
        botaoVoltar.setOnClickListener {
            val intent = Intent(this, MenuLoginActivity::class.java)
            startActivity(intent)
        }

        // Navigate to "Cadastrar Atendente"
        cadastrarAtendenteTextView.setOnClickListener {
            val intent = Intent(this, CadastroAtendenteActivity::class.java)
            startActivity(intent)
        }

        // Navigate to "Recuperar Senha"
        recuperarSenhaButton.setOnClickListener {
            val intent = Intent(this, AlterarSenhaActivity::class.java)
            intent.putExtra("origem", "atendente")
            startActivity(intent)
        }

        // Handle "Entrar" button click
        btnEntrar.setOnClickListener {
            val email = emailEditText.text.toString()
            val senha = senhaEditText.text.toString()

            // Validate email and password
            if (email.isBlank() || senha.isBlank() || !isEmailValid(email)) {
                showAlert("Digite um e-mail válido e uma senha")
            } else {
                // Navigate to the main menu
                val intent = Intent(this, MenuPrincipalActivity::class.java)
                intent.putExtra("tipoUsuario", "atendente")
                startActivity(intent)
                finish()
            }
        }
    }

    // Check if the email is valid
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Show alert dialog
    private fun showAlert(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Erro")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}
