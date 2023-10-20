package kind.sun.dev.coffeeworld.api

import kind.sun.dev.coffeeworld.data.remote.request.AuthRequest
import kind.sun.dev.coffeeworld.data.remote.request.LoginRequest
import kind.sun.dev.coffeeworld.data.remote.request.RegisterRequest
import kind.sun.dev.coffeeworld.data.remote.response.LoginResponse
import kind.sun.dev.coffeeworld.data.remote.response.MessageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("/auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<MessageResponse>

    @POST("/auth/forgot-password")
    suspend fun passwordReset(@Body authRequest: AuthRequest): Response<MessageResponse>

}