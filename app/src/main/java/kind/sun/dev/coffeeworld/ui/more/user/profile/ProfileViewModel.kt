package kind.sun.dev.coffeeworld.ui.more.user.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.data.model.response.common.MessageResponse
import kind.sun.dev.coffeeworld.data.model.response.user.UserResponse
import kind.sun.dev.coffeeworld.data.repository.UserRepository
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.network.NetworkHelper
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val networkHelper: NetworkHelper,
    private val validator: ProfileValidator
) : ViewModel(), ProfileContract.Service {

    val nameLiveData  by lazy { MutableLiveData<String>() }
    val addressLiveData by lazy { MutableLiveData<String>() }
    val phoneLiveData by lazy { MutableLiveData<String>() }

    val emailLiveData by lazy { MutableLiveData<String>() }
    val emailPasswordLiveData by lazy { MutableLiveData<String>() }

    val currentPasswordLiveData by lazy { MutableLiveData<String>() }
    val newPasswordLiveData by lazy { MutableLiveData<String>() }
    val retypeNewPasswordLiveData by lazy { MutableLiveData<String>() }

    val errorMessageLiveData  by lazy { MutableLiveData<String>() }
    val userResponseLiveData : LiveData<NetworkResult<UserResponse>>
        get() = userRepository.user
    val userUpdate: LiveData<NetworkResult<MessageResponse>>
        get() = userRepository.userUpdate

    override fun getUser() {
        viewModelScope.launch {
            if (networkHelper.isConnected) {
                userRepository.getUser()
            }
        }
    }

    override fun onUpdateAvatar(avatar: File) {
        viewModelScope.launch {
            if (networkHelper.isConnected) {
                userRepository.updateAvatar(avatar)
            }
        }
    }

    override fun onUpdateName() {
        val email = nameLiveData.value.toString().trim()
        validator.validateUpdateName(email).let {
            if (it.first) {
                viewModelScope.launch {
                    if (networkHelper.isConnected) {
                        userRepository.updateName(email)
                    }
                }
            } else {
                errorMessageLiveData.value = it.second
            }
        }
    }

    override fun onUpdateEmail() {
        val email = emailLiveData.value.toString().trim()
        val password = emailPasswordLiveData.value.toString().trim()
        validator.validatorUpdateEmail(email, password).let {
            if (it.first) {
                viewModelScope.launch {
                    if (networkHelper.isConnected) {
                        userRepository.updateEmail(email, password)
                    }
                }
            } else {
                errorMessageLiveData.value = it.second
            }
        }
    }

    override fun onUpdatePassword() {
        val currentPassword = currentPasswordLiveData.value.toString().trim()
        val newPassword = newPasswordLiveData.value.toString().trim()
        val reNewPassword = retypeNewPasswordLiveData.value.toString().trim()
        validator.validateUpdatePassword(currentPassword, newPassword, reNewPassword).let {
            if (it.first) {
                viewModelScope.launch {
                    if (networkHelper.isConnected) {
                        userRepository.updatePassword(currentPassword, newPassword)
                    }
                }
            } else {
                errorMessageLiveData.value = it.second
            }
        }
    }

    override fun onUpdateAddress() {
        val address = addressLiveData.value.toString().trim()
        validator.validateUpdateAddress(address).let {
            if (it.first) {
                viewModelScope.launch {
                    if (networkHelper.isConnected) {
                        userRepository.updateAddress(address)
                    }
                }
            } else {
                errorMessageLiveData.value = it.second
            }
        }
    }

    override fun onUpdatePhone() {
        val phoneNumber = phoneLiveData.value.toString().trim()
        validator.validateUpdatePhone(phoneNumber).let {
            if (it.first) {
                viewModelScope.launch {
                    if (networkHelper.isConnected) {
                        userRepository.updatePhone(phoneNumber)
                    }
                }
            } else {
                errorMessageLiveData.value = it.second
            }
        }
    }
}