package kind.sun.dev.coffeeworld.ui.auth.login

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.data.model.request.auth.LoginRequest
import kind.sun.dev.coffeeworld.data.model.response.auth.AuthResponse
import kind.sun.dev.coffeeworld.data.repository.AuthRepository
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    val usernameLiveData = MutableLiveData<String>("")
    val passwordLiveData = MutableLiveData<String>("")
    val errorMessageLiveData = MutableLiveData<String>("")
    val isPasswordVisible = MutableLiveData<Boolean>(false)

    val loginResponseLiveData: LiveData<NetworkResult<AuthResponse>>
        get() = authRepository.authLoginResponseLiveData

    fun onClickLogin() {
        val username = usernameLiveData.value.toString().trim()
        val password = passwordLiveData.value.toString().trim()
        val validationResult = validateDataInput(username, password)
        if (validationResult.first) {
            val loginRequest = LoginRequest(username, password)
            loginUser(loginRequest)
        } else {
            errorMessageLiveData.value = validationResult.second!!
        }
    }

    private fun validateDataInput(username: String, password: String): Pair<Boolean, String> {
        var result = Pair(true, "")
        when {
            TextUtils.isEmpty(username) || TextUtils.isEmpty(password) -> {
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
        }
        return result
    }


    private fun loginUser(loginRequest: LoginRequest) {
        viewModelScope.launch {
            authRepository.login(loginRequest)
        }
    }

    fun onShowPasswordChecked(isChecked: Boolean) {
        isPasswordVisible.value = isChecked
    }

}