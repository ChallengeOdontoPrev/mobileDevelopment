import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appodontoprev.data.model.response.ClinicResponse
import com.example.appodontoprev.data.model.response.SignupResponse
import com.example.appodontoprev.data.repository.AtendenteRepository
import kotlinx.coroutines.launch

class CadastroAtendenteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AtendenteRepository(application.applicationContext)

    private val _cadastroStatus = MutableLiveData<Result<SignupResponse>>()
    val cadastroStatus: LiveData<Result<SignupResponse>> = _cadastroStatus

    private val _clinicas = MutableLiveData<Result<List<ClinicResponse>>>()
    val clinicas: LiveData<Result<List<ClinicResponse>>> = _clinicas

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        carregarClinicas()
    }

    private fun carregarClinicas() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _clinicas.value = repository.getClinicas()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun cadastrarAtendente(
        email: String,
        senha: String,
        nome: String,
        rg: String,
        dataNascimento: String,
        clinicId: Long
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = AtendenteSignupRequest(
                    email = email,
                    password = senha,
                    name = nome,
                    rg = rg,
                    birthDate = dataNascimento,
                    clinicId = clinicId
                )
                _cadastroStatus.value = repository.cadastrarAtendente(request)
            } finally {
                _isLoading.value = false
            }
        }
    }
}