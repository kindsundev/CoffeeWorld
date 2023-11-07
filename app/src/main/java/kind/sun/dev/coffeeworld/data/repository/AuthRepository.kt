package kind.sun.dev.coffeeworld.data.repository

import androidx.lifecycle.MutableLiveData
import kind.sun.dev.coffeeworld.base.BaseRepository
import kind.sun.dev.coffeeworld.contract.AuthContract
import kind.sun.dev.coffeeworld.data.remote.api.AuthAPI
import kind.sun.dev.coffeeworld.data.remote.request.AuthRequest
import kind.sun.dev.coffeeworld.data.remote.request.LoginRequest
import kind.sun.dev.coffeeworld.data.remote.request.RegisterRequest
import kind.sun.dev.coffeeworld.data.remote.response.LoginResponse
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val remoteAPI: AuthAPI
): BaseRepository(), AuthContract.Service {

    private val _authLogin = MutableLiveData<NetworkResult<LoginResponse>>()
    override val loginResponse get() = _authLogin
    override val messageResponse get() = statusMessage

    override suspend fun handleLogin(loginRequest: LoginRequest) {
        performNetworkOperation(
            networkRequest = { remoteAPI.login(loginRequest) },
            networkResult = _authLogin
        )
    }

    override suspend fun handleRegistration(registerRequest: RegisterRequest) {
        performNetworkOperation(
            networkRequest = { remoteAPI.register(registerRequest) },
            networkResult = statusMessage
        )
    }

    override suspend fun handlePasswordReset(authRequest: AuthRequest) {
        performNetworkOperation(
            networkRequest = { remoteAPI.passwordReset(authRequest) },
            networkResult = statusMessage
        )
    }

}