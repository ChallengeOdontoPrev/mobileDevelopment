package com.example.appodontoprev


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
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

    }
}