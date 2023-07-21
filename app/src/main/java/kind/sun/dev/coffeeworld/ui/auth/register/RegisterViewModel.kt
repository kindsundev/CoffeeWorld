package kind.sun.dev.coffeeworld.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.data.model.request.auth.RegisterRequest
import kind.sun.dev.coffeeworld.data.repository.AuthRepository
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    val usernameLiveData = MutableLiveData<String>("")
    val passwordLiveData = MutableLiveData<String>("")
    val emailLiveData = MutableLiveData<String>("")
    val nameLiveData = MutableLiveData<String>("")
    val errorMessageLiveData = MutableLiveData<String>("")
    val isPasswordVisible = MutableLiveData<Boolean>(false)

    val registerResponseLiveData: LiveData<NetworkResult<String>>
        get() = authRepository.authRegisterResponseLiveData

    fun registerUser(registerRequest: RegisterRequest) {
        viewModelScope.launch {
            authRepository.register(registerRequest)
        }
    }

    fun onShowPasswordChecked(isChecked: Boolean) {
        isPasswordVisible.value = isChecked
    }
}