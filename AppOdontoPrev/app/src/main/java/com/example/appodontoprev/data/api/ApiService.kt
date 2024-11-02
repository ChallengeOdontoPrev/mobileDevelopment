import com.example.appodontoprev.data.model.request.DentistaSignupRequest
import com.example.appodontoprev.data.model.request.LoginRequest
import com.example.appodontoprev.data.model.response.ClinicResponse
import com.example.appodontoprev.data.model.response.LoginResponse
import com.example.appodontoprev.data.model.response.SignupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("auth/signup")
    suspend fun cadastrarDentista(@Body request: DentistaSignupRequest): Response<SignupResponse>

    @POST("auth/signup")
    suspend fun cadastrarAtendente(@Body request: AtendenteSignupRequest): Response<SignupResponse>

    @GET("clinics")
    suspend fun getClinicas(): Response<List<ClinicResponse>>

    // Novo endpoint
    @POST("auth/login")
    suspend fun realizarLogin(@Body request: LoginRequest): Response<LoginResponse>
}