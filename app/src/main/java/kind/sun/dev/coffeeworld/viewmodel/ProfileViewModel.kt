package kind.sun.dev.coffeeworld.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.base.BaseViewModel
import kind.sun.dev.coffeeworld.data.repository.UserRepository
import kind.sun.dev.coffeeworld.contract.ProfileContract
import kind.sun.dev.coffeeworld.data.local.model.UserModel
import kind.sun.dev.coffeeworld.utils.validator.ProfileValidator
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val profileValidator: ProfileValidator
) : BaseViewModel(), ProfileContract.ViewModel {

    val nameLiveData by lazy { MutableLiveData<String>() }
    val addressLiveData by lazy { MutableLiveData<String>() }
    val phoneLiveData by lazy { MutableLiveData<String>() }
    val emailLiveData by lazy { MutableLiveData<String>() }
    val emailPasswordLiveData by lazy { MutableLiveData<String>() }
    val currentPasswordLiveData by lazy { MutableLiveData<String>() }
    val newPasswordLiveData by lazy { MutableLiveData<String>() }
    val retypeNewPasswordLiveData by lazy { MutableLiveData<String>() }

    val userResponse get() = userRepository.user
    val messageResponse get() = userRepository.userUpdate

    override fun onFetchUser(onDataFromLocal: (UserModel?) -> Unit) {
        handleCheckAndRoute(
            conditionChecker = null,
            onPassedCheck = { viewModelScope.launch { userRepository.getUser() } },
            onFailedCheck = {

            }
        )
    }

    override fun onUpdateAvatar(avatar: File, message: (String) -> Unit) {
        handleCheckAndRoute(
            conditionChecker = null,
            onPassedCheck = { viewModelScope.launch { userRepository.updateAvatar(avatar) } },
            onFailedCheck = { message(it) }
        )
    }

    override fun onUpdateName(message: (String) -> Unit) {
        val name = nameLiveData.value.toString().trim()
        handleCheckAndRoute(
            conditionChecker = { profileValidator.validateUpdateName(name) },
            onPassedCheck = { viewModelScope.launch { userRepository.updateName(name) } },
            onFailedCheck = { message(it) }
        )
    }

    override fun onUpdateEmail(message: (String) -> Unit) {
        val email = emailLiveData.value.toString().trim()
        val password = emailPasswordLiveData.value.toString().trim()
        handleCheckAndRoute(
            conditionChecker = { profileValidator.validatorUpdateEmail(email, password) },
            onPassedCheck = { viewModelScope.launch { userRepository.updateEmail(email, password) } },
            onFailedCheck = { message(it) }
        )
    }

    override fun onUpdatePassword(message: (String) -> Unit) {
        val currentPassword = currentPasswordLiveData.value.toString().trim()
        val newPassword = newPasswordLiveData.value.toString().trim()
        val reNewPassword = retypeNewPasswordLiveData.value.toString().trim()
        handleCheckAndRoute(
            conditionChecker = {
                profileValidator.validateUpdatePassword(currentPassword, newPassword, reNewPassword)
            },
            onPassedCheck = {
                viewModelScope.launch { userRepository.updatePassword(currentPassword, newPassword) }
            },
            onFailedCheck = { message(it) }
        )
    }

    override fun onUpdateAddress(message: (String) -> Unit) {
        val address = addressLiveData.value.toString().trim()
        handleCheckAndRoute(
            conditionChecker = { profileValidator.validateUpdateAddress(address) },
            onPassedCheck = { viewModelScope.launch { userRepository.updateAddress(address) } },
            onFailedCheck = { message(it) }
        )
    }

    override fun onUpdatePhone(message: (String) -> Unit) {
        val phoneNumber = phoneLiveData.value.toString().trim()
        handleCheckAndRoute(
            conditionChecker = { profileValidator.validateUpdatePhone(phoneNumber) },
            onPassedCheck = { viewModelScope.launch { userRepository.updatePhone(phoneNumber) } },
            onFailedCheck = { message(it) }
        )
    }

}