package kind.sun.dev.coffeeworld.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.data.model.request.auth.AuthRequest
import kind.sun.dev.coffeeworld.data.model.request.auth.LoginRequest
import kind.sun.dev.coffeeworld.data.model.request.auth.RegisterRequest
import kind.sun.dev.coffeeworld.data.model.response.auth.LoginResponse
import kind.sun.dev.coffeeworld.data.model.response.common.MessageResponse
import kind.sun.dev.coffeeworld.data.repository.AuthRepository
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.network.NetworkHelper
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val networkHelper: NetworkHelper,
    private val authValidator: AuthValidator
): ViewModel(), AuthContract.Service {
    val loginResponse: LiveData<NetworkResult<LoginResponse>>
        get() = authRepository.authLogin
    val usernameLogin by lazy { MutableLiveData<String>() }
    val passwordLogin by lazy { MutableLiveData<String>() }
    val isPasswordLoginVisible by lazy { MutableLiveData(false) }

    val registerResponse: LiveData<NetworkResult<MessageResponse>>
        get() = authRepository.authRegister
    val nameRegister by lazy { MutableLiveData<String>() }
    val emailRegister by lazy { MutableLiveData<String>() }
    val usernameRegister by lazy { MutableLiveData<String>() }
    val passwordRegister by lazy { MutableLiveData<String>() }
    val confirmPasswordRegister by lazy { MutableLiveData<String>() }
    val isPasswordRegisterVisible by lazy { MutableLiveData(false) }

    val passwordResetResponse : LiveData<NetworkResult<MessageResponse>>
        get() = authRepository.authPasswordReset
    val usernameForgotPassword by lazy { MutableLiveData<String>() }
    val emailForgotPassword by lazy { MutableLiveData<String>() }

    val errorMessage by lazy { MutableLiveData<String>() }

    override fun onLogin() {
        val username = usernameLogin.value.toString().trim()
        val password = passwordLogin.value.toString().trim()
        authValidator.validateLoginInput(username, password).let {
            if (it.first) {
                requestLogin(LoginRequest(username, password))
            } else {
                errorMessage.value = it.second
            }
        }
    }

    private fun requestLogin(loginRequest: LoginRequest) {
        viewModelScope.launch {
            if (networkHelper.isConnected) {
                authRepository.login(loginRequest)
            } else {
                errorMessage.value = Constants.NO_INTERNET_CONNECTION
            }
        }
    }

    override fun onRegister() {
        val name = nameRegister.value.toString().trim()
        val email = emailRegister.value.toString().trim()
        val username = usernameRegister.value.toString().trim()
        val password = passwordRegister.value.toString().trim()
        val confirmPassword = confirmPasswordRegister.value.toString().trim()
        authValidator.validateRegisterInput(name, email, username, password, confirmPassword).let {
            if (it.first) {
                requestRegister(RegisterRequest(username, password, email, name))
            } else {
                errorMessage.value = it.second
            }
        }
    }

    private fun requestRegister(registerRequest: RegisterRequest) {
        viewModelScope.launch {
            viewModelScope.launch {
                if (networkHelper.isConnected) {
                    authRepository.register(registerRequest)
                } else {
                    errorMessage.value = Constants.NO_INTERNET_CONNECTION
                }
            }
        }
    }

    override fun onPasswordReset() {
        val username = usernameForgotPassword.value.toString().trim()
        val email = emailForgotPassword.value.toString().trim()
        authValidator.validateForgotPasswordInput(username, email).let {
            if (it.first) {
                sendPasswordReset(AuthRequest(username, email))
            } else {
                errorMessage.value = it.second
            }
        }
    }

    private fun sendPasswordReset(authRequest: AuthRequest) {
        viewModelScope.launch {
            if (networkHelper.isConnected) {
                authRepository.passwordReset(authRequest)
            } else {
                errorMessage.value = Constants.NO_INTERNET_CONNECTION
            }
        }
    }

    fun onShowPasswordLoginChecked(isChecked: Boolean) {
        isPasswordLoginVisible.value = isChecked
    }

    fun onShowPasswordRegisterChecked(isChecked: Boolean) {
        isPasswordRegisterVisible.value = isChecked
    }

}