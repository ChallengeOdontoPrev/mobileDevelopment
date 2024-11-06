package com.example.appodontoprev

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView


class ConsultasActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultas)

        val btnVolConsu = findViewById<ImageView>(R.id.btnVolConsu)
        val addConsulta = findViewById<ImageView>(R.id.addConsulta)
        val cardConsulta = findViewById<CardView>(R.id.cardConsulta)

        // Recuperar o tipo de usuário do SharedPreferences
        val sharedPref = getSharedPreferences("AppOdontoPrev", Context.MODE_PRIVATE)
        val tipoUsuario = sharedPref.getString("tipoUsuario", "") ?: ""

        // Controlar a visibilidade do botão addConsulta
        addConsulta.visibility = if (tipoUsuario == "atendente") {
            View.VISIBLE
        } else {
            View.GONE // ou View.INVISIBLE se quiser manter o espaço
        }

        // Listener para voltar
        btnVolConsu.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        // Listener para adicionar consulta (só será clicável se estiver visível)
        addConsulta.setOnClickListener {
            val intent = Intent(this, AgendamentoConsutaActivity::class.java)
            startActivity(intent)
        }

        // Listener para o card de consulta
        cardConsulta.setOnClickListener {
            val intent = Intent(this, ConsultaPacienteActivity::class.java)
            // Passar o tipo de usuário para a próxima activity
            intent.putExtra("tipoUsuario", tipoUsuario)
            startActivity(intent)
        }
    }
}