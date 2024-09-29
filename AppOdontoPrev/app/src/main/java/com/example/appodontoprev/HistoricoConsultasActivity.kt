package com.example.appodontoprev


import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class HistoricoConsultasActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historico_consultas)

        val btnVolHist = findViewById<ImageView>(R.id.btnVolHist)
        val tipoUsuario = intent.getStringExtra("tipoUsuario")

        btnVolHist.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            intent.putExtra("tipoUsuario", tipoUsuario)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

    }
}