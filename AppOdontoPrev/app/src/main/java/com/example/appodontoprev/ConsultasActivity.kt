package com.example.appodontoprev
import AppointmentAdapter
import AppointmentListResponse
import ConsultasViewModel
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ConsultasActivity: AppCompatActivity() {
    private val viewModel: ConsultasViewModel by viewModels()
    private lateinit var adapter: AppointmentAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmptyList: TextView
    private var tipoUsuario: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultas)

        // Recuperar o tipo de usuário
        val sharedPref = getSharedPreferences("AppOdontoPrev", Context.MODE_PRIVATE)
        tipoUsuario = sharedPref.getString("tipoUsuario", "") ?: ""

        initViews()
        setupRecyclerView()
        setupObservers()
        loadAppointments()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerViewAppointments)
        progressBar = findViewById(R.id.progressBar)
        tvEmptyList = findViewById(R.id.tvEmptyList)

        val btnVolConsu = findViewById<ImageView>(R.id.btnVolConsu)
        val addConsulta = findViewById<ImageView>(R.id.addConsulta)

        // Controlar a visibilidade do botão addConsulta
        addConsulta.visibility = if (tipoUsuario == "atendente") {
            View.VISIBLE
        } else {
            View.GONE
        }

        btnVolConsu.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        addConsulta.setOnClickListener {
            val intent = Intent(this, AgendamentoConsutaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        adapter = AppointmentAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ConsultasActivity)
            adapter = this@ConsultasActivity.adapter
            setHasFixedSize(false)
        }

        adapter.setOnItemClickListener { appointment ->
            navigateToAppointmentDetails(appointment)
        }
    }

    private fun setupObservers() {
        viewModel.appointments.observe(this) { result ->
            progressBar.visibility = View.GONE

            result.onSuccess { appointments ->
                if (appointments.isEmpty()) {
                    tvEmptyList.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    tvEmptyList.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    adapter.setAppointments(appointments)
                }
            }.onFailure { exception ->
                handleError(exception)
            }
        }
    }

    private fun loadAppointments() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        tvEmptyList.visibility = View.GONE
        viewModel.loadAppointments()
    }

    private fun navigateToAppointmentDetails(appointment: AppointmentListResponse) {
        val intent = Intent(this, ConsultaPacienteActivity::class.java).apply {
            putExtra("appointmentId", appointment.id)
            putExtra("patientName", appointment.patient)
            putExtra("appointmentDate", appointment.dateAppointment)
            putExtra("appointmentTime", appointment.timeAppointment)
            putExtra("procedureType", appointment.procedureType)
            putExtra("clinic", appointment.clinic)
            putExtra("tipoUsuario", tipoUsuario)
        }
        startActivity(intent)
    }

    private fun handleError(exception: Throwable) {
        val errorMessage = when (exception) {
            is java.net.UnknownHostException -> "Erro de conexão. Verifique sua internet."
            is java.net.SocketTimeoutException -> "Tempo de conexão esgotado. Tente novamente."
            else -> "Erro ao carregar consultas: ${exception.message}"
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        tvEmptyList.apply {
            text = "Não foi possível carregar as consultas"
            visibility = View.VISIBLE
        }
        recyclerView.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        loadAppointments()
    }
}