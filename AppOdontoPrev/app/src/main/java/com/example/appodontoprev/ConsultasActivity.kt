package com.example.appodontoprev


import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ConsultasActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_consultas)

        val btnVolConsu = findViewById<ImageView>(R.id.btnVolConsu)
        val tipoUsuario = intent.getStringExtra("tipoUsuario")
        val addConsulta = findViewById<ImageView>(R.id.addConsulta)

        btnVolConsu.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            intent.putExtra("tipoUsuario", tipoUsuario)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        addConsulta.setOnClickListener {
            val intent = Intent(this, AgendamentoConsutaActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}