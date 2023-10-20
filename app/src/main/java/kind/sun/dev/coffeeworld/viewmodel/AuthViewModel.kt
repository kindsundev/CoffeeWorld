package kind.sun.dev.coffeeworld.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.data.repository.AuthRepository
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
    private val authRepository: AuthRepository,
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

    val loginResponse get() = authRepository.authLogin
    val messageResponse get() = authRepository.messageResponse
    val validator get() = error

    override fun onLogin() {
        val username = usernameLogin.value.toString().trim()
        val password = passwordLogin.value.toString().trim()

        checkThenExecute(
            validator = { authValidator.validateLoginInput(username, password) }
        ) {
            viewModelScope.launch {
                authRepository.login(LoginRequest(username, password))
            }
        }
    }

    override fun onRegister() {
        val name = nameRegister.value.toString().trim()
        val email = emailRegister.value.toString().trim()
        val username = usernameRegister.value.toString().trim()
        val password = passwordRegister.value.toString().trim()
        val confirmPassword = confirmPasswordRegister.value.toString().trim()

        checkThenExecute(
            validator = { authValidator.validateRegisterInput(name, email, username, password, confirmPassword) }
        ) {
            viewModelScope.launch {
                authRepository.register(RegisterRequest(username, password, email, name))
            }
        }
    }

    override fun onPasswordReset() {
        val username = usernameForgotPassword.value.toString().trim()
        val email = emailForgotPassword.value.toString().trim()
        checkThenExecute(
            validator = { authValidator.validateForgotPasswordInput(username, email) }
        ) {
            viewModelScope.launch {
                authRepository.passwordReset(AuthRequest(username, email))
            }
        }
    }
}