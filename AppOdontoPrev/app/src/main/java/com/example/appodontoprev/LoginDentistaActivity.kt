package com.example.appodontoprev


import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class LoginDentistaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_dentista)

        // Find the Voltar button
        val botaoVoltar = findViewById<Button>(R.id.botaoVoltar)

        // Set click listener for Voltar button
        botaoVoltar.setOnClickListener {
            val intent = Intent(this, MenuLoginActivity::class.java)
            startActivity(intent)
        }

        val cadastrarDentistaTextView = findViewById<TextView>(R.id.cadastrarDentistaTextView)

        cadastrarDentistaTextView.setOnClickListener {
            val intent = Intent(this, CadastroDentistaActivity::class.java)
            startActivity(intent)
        }

        val recuperarSenhaButton = findViewById<TextView>(R.id.recuperarSenhaButton)

        recuperarSenhaButton.setOnClickListener {
            val intent = Intent(this, AlterarSenhaActivity::class.java)
            intent.putExtra("origem", "dentista")
            startActivity(intent)
        }

        val btnEntrar = findViewById<Button>(R.id.btnEntrar)
        btnEntrar.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            intent.putExtra("tipoUsuario", "dentista")
            startActivity(intent)
            finish() // Fecha a atividade de login
        }

        val emailEditText = findViewById<EditText>(R.id.inputEmailMedi)
        val senhaEditText = findViewById<EditText>(R.id.inputSenhaMedi)

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
                intent.putExtra("tipoUsuario", "dentista")
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