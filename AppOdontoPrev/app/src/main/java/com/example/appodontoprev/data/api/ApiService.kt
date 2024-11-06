import com.example.appodontoprev.data.model.request.DentistaSignupRequest
import com.example.appodontoprev.data.model.request.LoginRequest
import com.example.appodontoprev.data.model.request.PatientRequest
import com.example.appodontoprev.data.model.response.ClinicResponse
import com.example.appodontoprev.data.model.response.LoginResponse
import com.example.appodontoprev.data.model.response.PatientResponse
import com.example.appodontoprev.data.model.response.SignupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("auth/signup")
    suspend fun cadastrarDentista(@Body request: DentistaSignupRequest): Response<SignupResponse>

    @POST("auth/signup")
    suspend fun cadastrarAtendente(@Body request: AtendenteSignupRequest): Response<SignupResponse>

    @GET("clinics")
    suspend fun getClinicas(): Response<List<ClinicResponse>>

    @POST("auth/login")
    suspend fun realizarLogin(@Body request: LoginRequest): Response<LoginResponse>

    @GET("patients/{rg}")
    suspend fun getPatientByRg(@Path("rg") rg: String): Response<PatientResponse>

    @GET("proceduresType")
    suspend fun getProcedures(): Response<List<ProcedureResponse>>

    @GET("auth")
    suspend fun getDentists(@Query("role") role: String = "DENTISTA"): Response<List<DentistResponse>>

    @POST("patients")
    suspend fun createPatient(@Body request: PatientRequest): Response<PatientResponse>
}