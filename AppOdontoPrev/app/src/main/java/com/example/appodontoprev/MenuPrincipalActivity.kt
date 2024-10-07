package com.example.appodontoprev

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MenuPrincipalActivity : AppCompatActivity() {
    private lateinit var tipoUsuario: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        // Recuperar o tipo de usuário do SharedPreferences
        val sharedPref = getSharedPreferences("AppOdontoPrev", Context.MODE_PRIVATE)
        tipoUsuario = sharedPref.getString("tipoUsuario", "desconhecido") ?: "desconhecido"

        val iconPerson = findViewById<ImageView>(R.id.IconPerson)
        val btnConfig = findViewById<Button>(R.id.btnConfig)
        val btnHist = findViewById<Button>(R.id.btnHist)
        val btnConsultas = findViewById<Button>(R.id.btnConsultas)
        val btnSuporte = findViewById<Button>(R.id.suporte)
        val btnComUsar = findViewById<Button>(R.id.btnComUsar)

        // Atualizar o ícone baseado no tipo de usuário
        atualizarIcone(iconPerson)

        btnConfig.setOnClickListener {
            val intent = Intent(this, ConfiguracoesActivity::class.java)
            startActivity(intent)
        }

        btnHist.setOnClickListener {
            val intent = Intent(this, HistoricoConsultasActivity::class.java)
            startActivity(intent)
        }

        btnConsultas.setOnClickListener {
            val intent = Intent(this, ConsultasActivity::class.java)
            startActivity(intent)
        }

        btnComUsar.setOnClickListener {
            val intent = Intent(this, ComoUsarActivity::class.java)
            startActivity(intent)
        }

        btnSuporte.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.odontoprev.com.br/fale-conosco"))
            startActivity(browserIntent)
        }
    }

    private fun atualizarIcone(iconPerson: ImageView) {
        when (tipoUsuario) {
            "dentista" -> iconPerson.setImageResource(R.drawable.iconedentista)
            "atendente" -> iconPerson.setImageResource(R.drawable.iconeatendente)
        }
    }

    override fun onResume() {
        super.onResume()
        // Atualizar o ícone sempre que a atividade voltar ao primeiro plano
        val iconPerson = findViewById<ImageView>(R.id.IconPerson)
        atualizarIcone(iconPerson)
    }
}