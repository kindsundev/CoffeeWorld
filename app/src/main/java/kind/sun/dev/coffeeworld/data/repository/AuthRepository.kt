package kind.sun.dev.coffeeworld.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kind.sun.dev.coffeeworld.base.BaseRepository
import kind.sun.dev.coffeeworld.api.AuthService
import kind.sun.dev.coffeeworld.data.model.request.auth.AuthRequest
import kind.sun.dev.coffeeworld.data.model.request.auth.LoginRequest
import kind.sun.dev.coffeeworld.data.model.request.auth.RegisterRequest
import kind.sun.dev.coffeeworld.data.model.response.auth.LoginResponse
import kind.sun.dev.coffeeworld.data.model.response.common.MessageResponse
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService
): BaseRepository() {
    private val _authLogin = MutableLiveData<NetworkResult<LoginResponse>>()
    val authLogin: LiveData<NetworkResult<LoginResponse>>
        get() = _authLogin

    private val _authRegister = MutableLiveData<NetworkResult<MessageResponse>>()
    val authRegister: LiveData<NetworkResult<MessageResponse>>
        get() = _authRegister

    private val _authPasswordReset = MutableLiveData<NetworkResult<MessageResponse>>()
    val authPasswordReset: LiveData<NetworkResult<MessageResponse>>
        get() = _authPasswordReset

    suspend fun login(loginRequest: LoginRequest) {
        performNetworkOperation(_authLogin) {
            authService.login(loginRequest)
        }
    }

    suspend fun register(registerRequest: RegisterRequest) {
        performNetworkOperation(_authRegister) {
            authService.register(registerRequest)
        }
    }

    suspend fun passwordReset(authRequest: AuthRequest) {
        performNetworkOperation(_authPasswordReset) {
            authService.passwordReset(authRequest)
        }
    }

}