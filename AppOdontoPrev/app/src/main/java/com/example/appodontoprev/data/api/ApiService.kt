import com.example.appodontoprev.data.model.request.DentistaSignupRequest
import com.example.appodontoprev.data.model.response.ClinicResponse
import com.example.appodontoprev.data.model.response.SignupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("auth/signup")
    suspend fun cadastrarDentista(@Body request: DentistaSignupRequest): Response<SignupResponse>

    @GET("clinics")
    suspend fun getClinicas(): Response<List<ClinicResponse>>
}