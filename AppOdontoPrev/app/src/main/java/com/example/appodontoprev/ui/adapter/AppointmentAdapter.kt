import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.appodontoprev.R
import java.text.SimpleDateFormat
import java.util.Locale

class AppointmentAdapter : RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {
    private var appointments = mutableListOf<AppointmentListResponse>()
    private var onItemClickListener: ((AppointmentListResponse) -> Unit)? = null

    fun setAppointments(newAppointments: List<AppointmentListResponse>) {
        appointments.clear() // Limpa a lista atual
        appointments.addAll(newAppointments) // Adiciona todos os novos itens
        notifyDataSetChanged() // Notifica o adapter que os dados mudaram
    }

    fun setOnItemClickListener(listener: (AppointmentListResponse) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_appointment, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.bind(appointment)
    }

    override fun getItemCount() = appointments.size

    inner class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvPatientName: TextView = itemView.findViewById(R.id.nomePaciente)
        private val tvDate: TextView = itemView.findViewById(R.id.DataConsulta)
        private val tvTime: TextView = itemView.findViewById(R.id.horarioConsulta)
        private val cardView: CardView = itemView.findViewById(R.id.cardConsulta)

        fun bind(appointment: AppointmentListResponse) {
            tvPatientName.text = appointment.patient
            tvDate.text = formatDate(appointment.dateAppointment)
            tvTime.text = appointment.timeAppointment

            itemView.setOnClickListener {
                onItemClickListener?.invoke(appointment)
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
}