package com.example.appodontoprev

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.appodontoprev.AnaliseConsultaActivity
import com.example.appodontoprev.ConsultasActivity
import com.example.appodontoprev.R
import java.text.SimpleDateFormat
import java.util.Locale

class ConsultaPacienteActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulta_paciente)

        val btnVolConsuPac = findViewById<ImageView>(R.id.btnVolConsuPac)
        val tipoUsuario = intent.getStringExtra("tipoUsuario")
        val btnIniciarConsulta = findViewById<Button>(R.id.btnIniciarConsulta)
        val btnExcluirConsulta = findViewById<Button>(R.id.btnExcluirConsulta)

        // Recuperar os dados passados da consulta
        val nomePaciente = intent.getStringExtra("patientName") ?: ""
        val dataConsulta = intent.getStringExtra("appointmentDate") ?: ""
        val horaConsulta = intent.getStringExtra("appointmentTime") ?: ""
        val procedimento = intent.getStringExtra("procedureType") ?: ""

        // Atualizar as TextViews com os dados da consulta
        findViewById<TextView>(R.id.nomePaciente).text = nomePaciente
        findViewById<TextView>(R.id.DataConsulta).text = formatDate(dataConsulta)
        findViewById<TextView>(R.id.horarioConsulta).text = horaConsulta
        findViewById<TextView>(R.id.textViewProcedimento).text = procedimento

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

    private fun formatDate(dateStr: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return try {
            val date = inputFormat.parse(dateStr)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            dateStr
        }
    }
}