package com.example.appodontoprev

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appodontoprev.data.repository.AppointmentRepository
import com.example.appodontoprev.ui.adapter.AppointmentsAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ConsultasActivity : AppCompatActivity() {
    private lateinit var adapter: AppointmentsAdapter
    private lateinit var appointmentRepository: AppointmentRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnVolConsu: ImageView
    private lateinit var addConsulta: ImageView
    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultas)

        // Inicializar o repository
        appointmentRepository = AppointmentRepository(this)

        // Inicializar views
        setupViews()
        // Configurar listeners
        setupListeners()
        // Carregar consultas
        loadAppointments()
    }

    private fun setupViews() {
        // Encontrar views
        recyclerView = findViewById(R.id.recyclerViewConsultas)
        progressBar = findViewById(R.id.progressBar)
        btnVolConsu = findViewById(R.id.btnVolConsu)
        addConsulta = findViewById(R.id.addConsulta)

        // Configurar RecyclerView
        adapter = AppointmentsAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Controlar visibilidade do botão addConsulta baseado no tipo de usuário
        val sharedPref = getSharedPreferences("AppOdontoPrev", Context.MODE_PRIVATE)
        val tipoUsuario = sharedPref.getString("tipoUsuario", "") ?: ""
        addConsulta.visibility = if (tipoUsuario == "atendente") View.VISIBLE else View.GONE
    }

    private fun setupListeners() {
        // Botão voltar
        btnVolConsu.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        // Botão adicionar consulta
        addConsulta.setOnClickListener {
            startActivity(Intent(this, AgendamentoConsutaActivity::class.java))
        }

        // Click em item da lista
        adapter.onItemClick = { appointment ->
            val intent = Intent(this, ConsultaPacienteActivity::class.java).apply {
                putExtra("consultaId", appointment.id)
                putExtra("tipoUsuario", getSharedPreferences("AppOdontoPrev", Context.MODE_PRIVATE)
                    .getString("tipoUsuario", ""))
            }
            startActivity(intent)
        }
    }

    private fun loadAppointments() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        coroutineScope.launch {
            try {
                appointmentRepository.getAppointmentsWithPatientNames()
                    .onSuccess { appointments ->
                        if (appointments.isEmpty()) {
                            showEmptyState()
                        } else {
                            adapter.updateAppointments(appointments)
                            recyclerView.visibility = View.VISIBLE
                        }
                    }
                    .onFailure { exception ->
                        showError(exception.message ?: "Erro ao carregar consultas")
                    }
            } catch (e: Exception) {
                showError("Erro ao carregar consultas: ${e.message}")
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun showEmptyState() {
        // Aqui você pode adicionar uma view para mostrar quando não houver consultas
        recyclerView.visibility = View.GONE
        Toast.makeText(this, "Nenhuma consulta encontrada", Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String) {
        recyclerView.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        loadAppointments() // Recarrega as consultas quando voltar para a tela
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel() // Cancela as coroutines quando a activity for destruída
    }
}