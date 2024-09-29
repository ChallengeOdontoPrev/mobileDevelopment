package com.example.appodontoprev


import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ConsultaPacienteActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_consulta_paciente)

        val btnVolConsuPac = findViewById<ImageView>(R.id.btnVolConsuPac)
        val tipoUsuario = intent.getStringExtra("tipoUsuario")

        btnVolConsuPac.setOnClickListener {
            val intent = Intent(this, ConsultasActivity::class.java)
            intent.putExtra("tipoUsuario", tipoUsuario)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }


    }
}