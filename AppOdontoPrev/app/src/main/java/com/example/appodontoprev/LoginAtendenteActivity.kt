package com.example.appodontoprev

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class LoginAtendenteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_atendente)

        // Find the Voltar button
        val botaoVoltar = findViewById<Button>(R.id.botaoVoltar)

        // Set click listener for Voltar button
        botaoVoltar.setOnClickListener {
            val intent = Intent(this, MenuLoginActivity::class.java)
            startActivity(intent)
        }

        // Find the cadastrarAtendente TextView
        val cadastrarAtendenteTextView = findViewById<TextView>(R.id.cadastrarAtendente)

        // Set click listener for cadastrarAtendente TextView
        cadastrarAtendenteTextView.setOnClickListener {
            val intent = Intent(this, CadastroAtendenteActivity::class.java)
            startActivity(intent)
        }

        val recuperarSenhaButton = findViewById<TextView>(R.id.recuperarSenhaButton)

        recuperarSenhaButton.setOnClickListener {
            val intent = Intent(this, AlterarSenhaActivity::class.java)
            intent.putExtra("origem", "atendente")
            startActivity(intent)
        }

        val btnEntrar = findViewById<Button>(R.id.btnEntrar)
        btnEntrar.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            intent.putExtra("tipoUsuario", "atendente")
            startActivity(intent)
            finish() // Fecha a atividade de login
        }

    }
}