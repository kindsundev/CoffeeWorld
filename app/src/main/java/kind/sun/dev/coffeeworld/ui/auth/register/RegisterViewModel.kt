package kind.sun.dev.coffeeworld.ui.auth.register

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.data.model.request.auth.RegisterRequest
import kind.sun.dev.coffeeworld.data.model.response.auth.RegisterResponse
import kind.sun.dev.coffeeworld.data.repository.AuthRepository
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.network.NetworkHelper
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {
    val nameLiveData = MutableLiveData<String>("")
    val emailLiveData = MutableLiveData<String>("")
    val usernameLiveData = MutableLiveData<String>("")
    val passwordLiveData = MutableLiveData<String>("")
    val confirmPasswordLiveData = MutableLiveData<String>("")
    val errorMessageLiveData = MutableLiveData<String?>("")
    val isPasswordVisible = MutableLiveData<Boolean>(false)

    val registerResponseLiveData: LiveData<NetworkResult<RegisterResponse>>
        get() = authRepository.authRegisterResponseLiveData

    fun onClickRegister() {
        val name = nameLiveData.value.toString().trim()
        val email = emailLiveData.value.toString().trim()
        val username = usernameLiveData.value.toString().trim()
        val password = passwordLiveData.value.toString().trim()
        val confirmPassword = confirmPasswordLiveData.value.toString().trim()

        if (password != confirmPassword) {
            errorMessageLiveData.value = "Password confirmation does not match"
        } else {
            val validationResult = validateDataInput(name, email, username, password)
            if (validationResult.first) {
                val registerRequest = RegisterRequest(username, password, email, name)
                registerUser(registerRequest)
            } else {
                errorMessageLiveData.value = validationResult.second
            }
        }
    }
    private fun validateDataInput(
        name: String,
        email: String,
        username: String,
        password: String
    ): Pair<Boolean, String> {
        var result = Pair(true, "")
        when {
            TextUtils.isEmpty(username) || TextUtils.isEmpty(password)
                    || TextUtils.isEmpty(email) || TextUtils.isEmpty(name) -> {
                result = Pair(false, "Please provide all the credentials")
            }
            username.contains("\\s".toRegex()) -> {
                result = Pair(false, "Username must not have spaces")
            }
            username.endsWith(".") -> {
                result = Pair(false, "Username cannot end with dot")
            }
            password.length < 8 -> {
                result = Pair(false, "Password must be more than 8 characters")
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                result = Pair(false, "Please provide a valid email")
            }
            TextUtils.isDigitsOnly(name) -> {
                result = Pair(false, "Name cannot be a number")
            }
        }
        return result
    }

    private fun registerUser(registerRequest: RegisterRequest) {
        viewModelScope.launch {
            if (networkHelper.isConnected) {
                authRepository.register(registerRequest)
            } else {
                errorMessageLiveData.value = Constants.NO_INTERNET_CONNECTION
            }
        }
    }

    fun onShowPasswordChecked(isChecked: Boolean) {
        isPasswordVisible.value = isChecked
    }
}