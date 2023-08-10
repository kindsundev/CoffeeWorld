package kind.sun.dev.coffeeworld.ui.more.user.profile.password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.data.model.response.user.UserUpdateResponse
import kind.sun.dev.coffeeworld.data.repository.UserRepository
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.network.NetworkHelper
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordDialogViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val networkHelper: NetworkHelper
): ViewModel() {
    val userUpdateResponseLiveData: LiveData<NetworkResult<UserUpdateResponse>>
        get() = userRepository.userUpdateResponseLiveData

    val currentPasswordLiveData = MutableLiveData<String>("")
    val newPasswordLiveData = MutableLiveData<String>("")
    val retypeNewPasswordLiveData = MutableLiveData<String>("")
    val errorMessageLiveData = MutableLiveData<String?>("")

    fun onUpdatePassword() {
        val currentPassword = currentPasswordLiveData.value.toString().trim()
        val newPassword = newPasswordLiveData.value.toString().trim()
        val reNewPassword = retypeNewPasswordLiveData.value.toString().trim()
        val validationResult = validateDataInput(currentPassword, newPassword, reNewPassword)
        if (validationResult.first) {
            updatePassword(currentPassword, newPassword)
        } else {
            errorMessageLiveData.value = validationResult.second
        }
    }

    private fun validateDataInput(
        currentPassword: String,
        newPassword: String,
        reNewPassword: String
    ): Pair<Boolean, String> {
        var result = Pair(true, "")
        when {
            currentPassword.length < 8 || newPassword.length < 8 || reNewPassword.length < 8 -> {
                result = Pair(false, "Password must be more than 8 characters")
            }
            newPassword != reNewPassword -> {
                result = Pair(false, "Both new passwords don't match")
            }
        }
        return result
    }

    private fun updatePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            if (networkHelper.isConnected) {
                userRepository.updatePassword(currentPassword, newPassword)
            }
        }
    }
}