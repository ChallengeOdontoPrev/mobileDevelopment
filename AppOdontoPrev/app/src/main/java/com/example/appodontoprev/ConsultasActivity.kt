package com.example.appodontoprev

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class ConsultasActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_consultas)

        val btnVolConsu = findViewById<ImageView>(R.id.btnVolConsu)
        val tipoUsuario = intent.getStringExtra("tipoUsuario")
        val addConsulta = findViewById<ImageView>(R.id.addConsulta)
        val cardConsulta = findViewById<CardView>(R.id.cardConsulta)

        btnVolConsu.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            intent.putExtra("tipoUsuario", tipoUsuario)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        addConsulta.setOnClickListener {
            val intent = Intent(this, AgendamentoConsutaActivity::class.java)
            intent.putExtra("tipoUsuario", tipoUsuario)
            startActivity(intent)
        }

        cardConsulta.setOnClickListener {
            val intent = Intent(this, ConsultaPacienteActivity::class.java)
            intent.putExtra("tipoUsuario", tipoUsuario)
            startActivity(intent)
        }
    }
}