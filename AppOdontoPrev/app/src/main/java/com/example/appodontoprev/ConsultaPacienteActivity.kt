package com.example.appodontoprev

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ConsultaPacienteActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_consulta_paciente)

        val btnVolConsuPac = findViewById<ImageView>(R.id.btnVolConsuPac)
        val tipoUsuario = intent.getStringExtra("tipoUsuario")
        val btnIniciarConsulta = findViewById<Button>(R.id.btnIniciarConsulta)
        val btnExcluirConsulta = findViewById<Button>(R.id.btnExcluirConsulta)

        btnVolConsuPac.setOnClickListener {
            val intent = Intent(this, ConsultasActivity::class.java)
            intent.putExtra("tipoUsuario", tipoUsuario)
            startActivity(intent)
            finish()
        }

        btnIniciarConsulta.setOnClickListener {
            val intent = Intent(this, AnaliseConsultaActivity::class.java)
            intent.putExtra("tipoUsuario", tipoUsuario)
            startActivity(intent)
        }

        btnExcluirConsulta.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Em breve")
                .setMessage("Ainda não é possível excluir consulta, em breve essa opção estará funcionando.")
                .setPositiveButton("OK", null)
                .show()
        }
    }
}