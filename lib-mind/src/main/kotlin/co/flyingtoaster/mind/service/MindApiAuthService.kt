package co.flyingtoaster.mind.service

import co.flyingtoaster.mind.service.model.LoginRequest
import co.flyingtoaster.mind.service.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MindApiAuthService {

    @POST("auth/login")
    @Headers("Content-Type: application/json")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}