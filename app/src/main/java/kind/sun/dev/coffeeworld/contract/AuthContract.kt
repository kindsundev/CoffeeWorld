package kind.sun.dev.coffeeworld.contract

import androidx.lifecycle.LiveData
import kind.sun.dev.coffeeworld.data.remote.request.AuthRequest
import kind.sun.dev.coffeeworld.data.remote.request.LoginRequest
import kind.sun.dev.coffeeworld.data.remote.request.RegisterRequest
import kind.sun.dev.coffeeworld.data.remote.response.LoginResponse
import kind.sun.dev.coffeeworld.data.remote.response.MessageResponse
import kind.sun.dev.coffeeworld.utils.api.NetworkResult

interface AuthContract {

    interface Validator {
        fun validateRegisterInput(
            name: String,
            email: String,
            username: String,
            password: String,
            confirmPassword: String
        ): Pair<Boolean, String>

        fun validateLoginInput(username: String, password: String): Pair<Boolean, String>

        fun validateForgotPasswordInput(username: String, email: String): Pair<Boolean, String>
    }


    interface ViewModel {
        fun onLogin(onFailedMessage: (String) -> Unit)

        fun onRegister(onFailedMessage: (String) -> Unit)

        fun onPasswordReset(onFailedMessage: (String) -> Unit)
    }

    interface  Service {
        val loginResponse: LiveData<NetworkResult<LoginResponse>>
        val messageResponse: LiveData<NetworkResult<MessageResponse>>

        suspend fun handleLogin(loginRequest: LoginRequest)

        suspend fun handleRegistration(registerRequest: RegisterRequest)

        suspend fun handlePasswordReset(authRequest: AuthRequest)
    }
}