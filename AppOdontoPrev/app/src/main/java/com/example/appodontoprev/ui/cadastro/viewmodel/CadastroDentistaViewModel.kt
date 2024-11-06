import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.appodontoprev.data.model.request.DentistaSignupRequest
import com.example.appodontoprev.data.model.response.ClinicResponse
import com.example.appodontoprev.data.model.response.SignupResponse
import com.example.appodontoprev.data.repository.DentistRepository
import kotlinx.coroutines.launch

class CadastroDentistaViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = DentistRepository(application.applicationContext)

    // LiveData para o status do cadastro
    private val _cadastroStatus = MutableLiveData<Result<SignupResponse>>()
    val cadastroStatus: LiveData<Result<SignupResponse>> = _cadastroStatus

    // LiveData para a lista de clínicas
    private val _clinicas = MutableLiveData<Result<List<ClinicResponse>>>()
    val clinicas: LiveData<Result<List<ClinicResponse>>> = _clinicas

    // LiveData para o estado de carregamento
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

    fun cadastrarDentista(
        email: String,
        senha: String,
        nome: String,
        rg: String,
        dataNascimento: String,
        cro: String,
        clinicId: Long
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = DentistaSignupRequest(
                    email = email,
                    password = senha,
                    name = nome,
                    rg = rg,
                    birthDate = dataNascimento,
                    cro = cro,
                    clinicId = clinicId
                )
                _cadastroStatus.value = repository.cadastrarDentista(request)
            } finally {
                _isLoading.value = false
            }
        }
    }
}