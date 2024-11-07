package com.example.appodontoprev.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appodontoprev.R
import com.example.appodontoprev.data.model.response.AppointmentWithPatientName

class AppointmentsAdapter : RecyclerView.Adapter<AppointmentViewHolder>() {
    private var appointments = listOf<AppointmentWithPatientName>()
    var onItemClick: ((AppointmentWithPatientName) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_consulta, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.bind(appointment, onItemClick)
    }

    override fun getItemCount(): Int = appointments.size

    fun updateAppointments(newAppointments: List<AppointmentWithPatientName>) {
        appointments = newAppointments
        notifyDataSetChanged()
    }
}