package com.example.appodontoprev

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Locale

class ConsultaPacienteActivity: AppCompatActivity() {
    private var tipoUsuario: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulta_paciente)

        // Recuperar os dados passados pela intent
        tipoUsuario = intent.getStringExtra("tipoUsuario") ?: ""
        val nomePaciente = intent.getStringExtra("patientName") ?: ""
        val dataConsulta = intent.getStringExtra("appointmentDate") ?: ""
        val horaConsulta = intent.getStringExtra("appointmentTime") ?: ""
        val procedimento = intent.getStringExtra("procedureType") ?: ""

        // Inicializar as views
        val btnVolConsuPac = findViewById<ImageView>(R.id.btnVolConsuPac)
        val btnIniciarConsulta = findViewById<Button>(R.id.btnIniciarConsulta)
        val btnExcluirConsulta = findViewById<Button>(R.id.btnExcluirConsulta)

        // Atualizar os TextViews com os dados da consulta
        findViewById<TextView>(R.id.nomePaciente).text = nomePaciente
        findViewById<TextView>(R.id.DataConsulta).text = formatDate(dataConsulta)
        findViewById<TextView>(R.id.horarioConsulta).text = horaConsulta
        findViewById<TextView>(R.id.textViewProcedimento).text = procedimento

        // Configurar listeners dos botões
        setupListeners(btnVolConsuPac, btnIniciarConsulta, btnExcluirConsulta)
    }

    private fun setupListeners(
        btnVolConsuPac: ImageView,
        btnIniciarConsulta: Button,
        btnExcluirConsulta: Button
    ) {
        btnVolConsuPac.setOnClickListener {
            navigateBack()
        }

        btnIniciarConsulta.setOnClickListener {
            val intent = Intent(this, AnaliseConsultaActivity::class.java).apply {
                putExtra("patientName", intent.getStringExtra("patientName"))
                putExtra("appointmentDate", intent.getStringExtra("appointmentDate"))
                putExtra("appointmentTime", intent.getStringExtra("appointmentTime"))
                putExtra("procedureType", intent.getStringExtra("procedureType"))
                putExtra("tipoUsuario", tipoUsuario)
            }
            startActivity(intent)
        }

        btnExcluirConsulta.setOnClickListener {
            showExcluirConsultaDialog()
        }
    }

    private fun showExcluirConsultaDialog() {
        AlertDialog.Builder(this)
            .setTitle("Em breve")
            .setMessage("Ainda não é possível excluir consulta, em breve essa opção estará funcionando.")
            .setPositiveButton("OK", null)
            .show()
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

    private fun navigateBack() {
        val intent = Intent(this, ConsultasActivity::class.java)
        intent.putExtra("tipoUsuario", tipoUsuario)
        startActivity(intent)
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        navigateBack()
    }
}