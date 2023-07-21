package kind.sun.dev.coffeeworld.ui.auth

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.data.model.request.auth.LoginRequest
import kind.sun.dev.coffeeworld.data.model.request.auth.RegisterRequest
import kind.sun.dev.coffeeworld.data.model.response.auth.AuthResponse
import kind.sun.dev.coffeeworld.data.repository.AuthRepository
import kind.sun.dev.coffeeworld.utils.NetworkResult
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val authLoginResponseLiveData: LiveData<NetworkResult<AuthResponse>>
        get() = authRepository.authLoginResponseLiveData

    val authRegisterResponseLiveData: LiveData<NetworkResult<String>>
        get() = authRepository.authRegisterResponseLiveData


    fun loginUser(loginRequest: LoginRequest) {
        viewModelScope.launch {
            authRepository.login(loginRequest)
        }
    }

    fun registerUser(registerRequest: RegisterRequest) {
        viewModelScope.launch {
            authRepository.register(registerRequest)
        }
    }

    fun validateLoginCredentials(loginRequest: LoginRequest): Pair<Boolean, String> {
        var result = Pair(true, "")
        when {
            isTextEmpty(loginRequest.username) || isTextEmpty(loginRequest.password) -> {
                result = Pair(false, "Please provide all the credentials")
            }
            loginRequest.username.matches(Regex("\\S+")) -> {
                result = Pair(false, "Username must not have spaces")
            }
            loginRequest.username.endsWith(".") -> {
                result = Pair(false, "Username cannot end with dot")
            }
            loginRequest.password.length < 8 -> {
                result = Pair(false, "Password must be more than 8 characters")
            }
        }
        return result
    }

    fun validateRegisterCredentials(registerRequest: RegisterRequest): Pair<Boolean, String> {
        var result = Pair(true, "")
        when {
            isTextEmpty(registerRequest.username) || isTextEmpty(registerRequest.password)
                    || isTextEmpty(registerRequest.email) || isTextEmpty(registerRequest.name) -> {
                result = Pair(false, "Please provide all the credentials")
            }
            registerRequest.username.matches(Regex("\\S+")) -> {
                result = Pair(false, "Username must not have spaces")
            }
            registerRequest.username.endsWith(".") -> {
                result = Pair(false, "Username cannot end with dot")
            }
            registerRequest.password.length < 8 -> {
                result = Pair(false, "Password must be more than 8 characters")
            }
            !Patterns.EMAIL_ADDRESS.matcher(registerRequest.email).matches() -> {
                result = Pair(false, "Please provide a valid email")
            }
            !TextUtils.isDigitsOnly(registerRequest.name) -> {
                result = Pair(false, "Name cannot be a number")
            }
        }
        return result
    }

    private fun isTextEmpty(data: String): Boolean = TextUtils.isEmpty(data)

}