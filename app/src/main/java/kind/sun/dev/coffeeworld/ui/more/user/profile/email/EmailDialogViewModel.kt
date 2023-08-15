package kind.sun.dev.coffeeworld.ui.more.user.profile.email

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.data.model.response.common.MessageResponse
import kind.sun.dev.coffeeworld.data.repository.UserRepository
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.network.NetworkHelper
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailDialogViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {
    val userUpdate: LiveData<NetworkResult<MessageResponse>>
        get() = userRepository.userUpdate

    val emailLiveData = MutableLiveData<String>("")
    val passwordLiveData = MutableLiveData<String>("")
    val errorMessageLiveData = MutableLiveData<String?>("")

    fun onUpdateEmail() {
        val email = emailLiveData.value.toString().trim()
        val password = passwordLiveData.value.toString().trim()
        val validationResult = validateDataInput(email, password)
        if (validationResult.first) {
            updateEmail(email, password)
        } else {
            errorMessageLiveData.value = validationResult.second
        }
    }

    private fun validateDataInput(email: String, password: String): Pair<Boolean, String> {
        var result = Pair(true, "")
        when {
            TextUtils.isEmpty(email) -> {
                result = Pair(false, "Please provide your email")
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                result = Pair(false, "Please provide a valid email")
            }
            password.length < 8 -> {
                result = Pair(false, "Password must be more than 8 characters")
            }
        }
        return result
    }

    private fun updateEmail(email: String, password: String) {
        viewModelScope.launch {
            if (networkHelper.isConnected) {
                userRepository.updateEmail(email, password)
            }
        }
    }
}