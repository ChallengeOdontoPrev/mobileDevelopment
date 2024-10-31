package com.example.appodontoprev

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class LoginDentistaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_dentista)

        val botaoVoltar = findViewById<Button>(R.id.botaoVoltar)
        val cadastrarDentistaTextView = findViewById<TextView>(R.id.cadastrarDentistaTextView)
        val recuperarSenhaButton = findViewById<TextView>(R.id.recuperarSenhaButton)
        val btnEntrar = findViewById<Button>(R.id.btnEntrar)
        val emailEditText = findViewById<EditText>(R.id.inputEmailMedi)
        val senhaEditText = findViewById<EditText>(R.id.inputSenhaMedi)

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

            if (email.isBlank() || senha.isBlank() || !isEmailValid(email)) {
                showAlert("Digite um e-mail válido e uma senha")
            } else {
                // Salvar o tipo de usuário
                val sharedPref = getSharedPreferences("AppOdontoPrev", Context.MODE_PRIVATE)
                with (sharedPref.edit()) {
                    putString("tipoUsuario", "dentista")
                    apply()
                }

                val intent = Intent(this, MenuPrincipalActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showAlert(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Erro")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}