package com.example.appodontoprev.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appodontoprev.R
import com.example.appodontoprev.data.model.response.AppointmentWithPatientName
import java.text.SimpleDateFormat
import java.util.Locale

class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val txtNomePaciente: TextView = itemView.findViewById(R.id.txtNomePaciente)
    private val txtData: TextView = itemView.findViewById(R.id.txtData)
    private val txtHorario: TextView = itemView.findViewById(R.id.txtHorario)

    fun bind(appointment: AppointmentWithPatientName, onItemClick: ((AppointmentWithPatientName) -> Unit)? = null) {
        txtNomePaciente.text = appointment.patientName
        txtData.text = formatDate(appointment.dateAppointment)
        txtHorario.text = appointment.timeAppointment

        itemView.setOnClickListener {
            onItemClick?.invoke(appointment)
        }
    }

    private fun formatDate(date: String): String {
        return try {
            val originalFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val targetFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val originalDate = originalFormat.parse(date)
            targetFormat.format(originalDate!!)
        } catch (e: Exception) {
            date
        }
    }

    companion object {
        fun create(parent: View): AppointmentViewHolder {
            return AppointmentViewHolder(parent)
        }
    }
}