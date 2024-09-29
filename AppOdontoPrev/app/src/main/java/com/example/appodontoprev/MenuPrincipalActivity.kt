package com.example.appodontoprev

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MenuPrincipalActivity : AppCompatActivity() {
    private lateinit var tipoUsuario: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal)

        tipoUsuario = intent.getStringExtra("tipoUsuario") ?: "desconhecido"
        val iconPerson = findViewById<ImageView>(R.id.IconPerson)
        val btnConfig = findViewById<Button>(R.id.btnConfig)
        val btnHist = findViewById<Button>(R.id.btnHist)
        val btnConsultas = findViewById<Button>(R.id.btnConsultas)
        val btnSuporte = findViewById<Button>(R.id.suporte)
        val btnComUsar = findViewById<Button>(R.id.btnComUsar)


        when (tipoUsuario) {
            "dentista" -> iconPerson.setImageResource(R.drawable.iconedentista)
            "atendente" -> iconPerson.setImageResource(R.drawable.iconeatendente)
        }

        btnConfig.setOnClickListener {
            val intent = Intent(this, ConfiguracoesActivity::class.java)
            intent.putExtra("tipoUsuario", tipoUsuario)
            startActivity(intent)
        }

        btnHist.setOnClickListener {
            val intent = Intent(this, HistoricoConsultasActivity::class.java)
            intent.putExtra("tipoUsuario", tipoUsuario)
            startActivity(intent)
        }

        btnConsultas.setOnClickListener {
            val intent = Intent(this, ConsultasActivity::class.java)
            intent.putExtra("tipoUsuario", tipoUsuario)
            startActivity(intent)
        }

        btnComUsar.setOnClickListener {
            val intent = Intent(this, ComoUsarActivity::class.java)
            intent.putExtra("tipoUsuario", tipoUsuario)
            startActivity(intent)
        }

        // Adicionando o clique para o botão "Fale com a Odontoprev"
        btnSuporte.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.odontoprev.com.br/fale-conosco"))
            startActivity(browserIntent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        tipoUsuario = intent?.getStringExtra("tipoUsuario") ?: tipoUsuario
        val iconPerson = findViewById<ImageView>(R.id.IconPerson)
        when (tipoUsuario) {
            "dentista" -> iconPerson.setImageResource(R.drawable.iconedentista)
            "atendente" -> iconPerson.setImageResource(R.drawable.iconeatendente)
        }
    }
}
