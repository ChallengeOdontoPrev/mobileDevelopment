import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.appodontoprev.data.repository.AppointmentRepository
import kotlinx.coroutines.launch

// ui/viewmodel/ConsultasViewModel.kt
class ConsultasViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AppointmentRepository(application.applicationContext)

    private val _appointments = MutableLiveData<Result<List<AppointmentListResponse>>>()
    val appointments: LiveData<Result<List<AppointmentListResponse>>> = _appointments

    fun loadAppointments() {
        viewModelScope.launch {
            try {
                _appointments.value = repository.getAppointments()
            } catch (e: Exception) {
                _appointments.value = Result.failure(e)
            }
        }
    }
}