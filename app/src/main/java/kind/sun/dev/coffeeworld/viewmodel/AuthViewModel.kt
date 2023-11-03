package kind.sun.dev.coffeeworld.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.data.remote.request.AuthRequest
import kind.sun.dev.coffeeworld.data.remote.request.LoginRequest
import kind.sun.dev.coffeeworld.data.remote.request.RegisterRequest
import kind.sun.dev.coffeeworld.contract.AuthContract
import kind.sun.dev.coffeeworld.utils.validator.AuthValidator
import kind.sun.dev.coffeeworld.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: AuthContract.Service,
    private val authValidator: AuthValidator
): BaseViewModel(), AuthContract.ViewModel {

    val usernameLogin by lazy { MutableLiveData<String>() }
    val passwordLogin by lazy { MutableLiveData<String>() }
    val nameRegister by lazy { MutableLiveData<String>() }
    val emailRegister by lazy { MutableLiveData<String>() }
    val usernameRegister by lazy { MutableLiveData<String>() }
    val passwordRegister by lazy { MutableLiveData<String>() }
    val confirmPasswordRegister by lazy { MutableLiveData<String>() }
    val usernameForgotPassword by lazy { MutableLiveData<String>() }
    val emailForgotPassword by lazy { MutableLiveData<String>() }

    val loginResponse get() = authService.loginResponse
    val messageResponse get() = authService.messageResponse

    override fun onLogin(onFailedMessage: (String) -> Unit) {
        val username = usernameLogin.value.toString().trim()
        val password = passwordLogin.value.toString().trim()
        handleCheckAndRoute(
            conditionChecker = {
                authValidator.validateLoginInput(username, password)
            },
            onPassedCheck = {
                viewModelScope.launch { authService.handleLogin(LoginRequest(username, password)) }
            },
            onFailedCheck = { reason, _ -> onFailedMessage(reason) }
        )
    }

    override fun onRegister(onFailedMessage: (String) -> Unit) {
        val name = nameRegister.value.toString().trim()
        val email = emailRegister.value.toString().trim()
        val username = usernameRegister.value.toString().trim()
        val password = passwordRegister.value.toString().trim()
        val confirmPassword = confirmPasswordRegister.value.toString().trim()
        handleCheckAndRoute(
            conditionChecker = {
                authValidator.validateRegisterInput(name, email, username, password, confirmPassword)
            },
            onPassedCheck = {
                viewModelScope.launch {
                    authService.handleRegistration(RegisterRequest(username, password, email, name))
                }
            },
            onFailedCheck = { reason, _ -> onFailedMessage(reason) }
        )
    }

    override fun onPasswordReset(onFailedMessage: (String) -> Unit) {
        val username = usernameForgotPassword.value.toString().trim()
        val email = emailForgotPassword.value.toString().trim()
        handleCheckAndRoute(
            conditionChecker = {
                authValidator.validateForgotPasswordInput(username, email)
            },
            onPassedCheck = {
                viewModelScope.launch {
                    authService.handlePasswordReset(AuthRequest(username, email))
                }
            },
            onFailedCheck = { reason, _ -> onFailedMessage(reason) }
        )
    }
}