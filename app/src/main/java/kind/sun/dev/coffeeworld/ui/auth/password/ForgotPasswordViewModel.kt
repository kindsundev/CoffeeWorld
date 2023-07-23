package kind.sun.dev.coffeeworld.ui.auth.password

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.data.model.request.auth.AuthRequest
import kind.sun.dev.coffeeworld.data.model.response.auth.AuthResponse
import kind.sun.dev.coffeeworld.data.repository.AuthRepository
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.network.NetworkHelper
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val networkHelper: NetworkHelper
): ViewModel() {
    val usernameLiveData = MutableLiveData<String>("")
    val emailLiveData = MutableLiveData<String>("")
    val errorMessageLiveData = MutableLiveData<String>("")

    val passwordResetResponseLiveData : LiveData<NetworkResult<AuthResponse>>
        get() = authRepository.authPasswordResetResponseLiveData

    fun onClickConfirm() {
        val username = usernameLiveData.value.toString().trim()
        val email = emailLiveData.value.toString().trim()
        val validationResult = validateDataInput(username, email)
        if (validationResult.first) {
            val registerRequest = AuthRequest(username, email)
            passwordReset(registerRequest)
        } else {
            errorMessageLiveData.value = validationResult.second!!
        }
    }

    private fun validateDataInput(username: String, email: String): Pair<Boolean, String> {
        var result = Pair(true, "")
        when {
            TextUtils.isEmpty(username) || TextUtils.isEmpty(email) -> {
                result = Pair(false, "Please provide all the credentials")
            }
            username.contains("\\s".toRegex()) -> {
                result = Pair(false, "Username must not have spaces")
            }
            username.endsWith(".") -> {
                result = Pair(false, "Username cannot end with dot")
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                result = Pair(false, "Please provide a valid email")
            }
        }
        return result
    }

    private fun passwordReset(authRequest: AuthRequest) {
        viewModelScope.launch {
            if (networkHelper.isConnected) {
                authRepository.passwordReset(authRequest)
            } else {
                errorMessageLiveData.value = Constants.NO_INTERNET_CONNECTION
            }
        }
    }

}