package kind.sun.dev.coffeeworld.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.base.BaseViewModel
import kind.sun.dev.coffeeworld.data.repository.UserRepository
import kind.sun.dev.coffeeworld.contract.ProfileContract
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

    val userResponseLiveData get() = userRepository.user
    val messageResponse get() = userRepository.userUpdate
    val validator get() = error

    override fun getUser() {
        checkThenExecute(null) {
            viewModelScope.launch {
                userRepository.getUser()
            }
        }
    }

    override fun onUpdateAvatar(avatar: File) {
        checkThenExecute(null) {
            viewModelScope.launch {
                userRepository.updateAvatar(avatar)
            }
        }
    }

    override fun onUpdateName() {
        val name = nameLiveData.value.toString().trim()
        checkThenExecute(
            validator = { profileValidator.validateUpdateName(name) }
        ) {
            viewModelScope.launch {
                userRepository.updateName(name)
            }
        }
    }

    override fun onUpdateEmail() {
        val email = emailLiveData.value.toString().trim()
        val password = emailPasswordLiveData.value.toString().trim()

        checkThenExecute(
            validator = { profileValidator.validatorUpdateEmail(email, password) }
        ) {
            viewModelScope.launch {
                userRepository.updateEmail(email, password)
            }
        }
    }

    override fun onUpdatePassword() {
        val currentPassword = currentPasswordLiveData.value.toString().trim()
        val newPassword = newPasswordLiveData.value.toString().trim()
        val reNewPassword = retypeNewPasswordLiveData.value.toString().trim()

        checkThenExecute(
            validator = {  profileValidator.validateUpdatePassword(currentPassword, newPassword, reNewPassword) }
        ) {
            viewModelScope.launch {
                userRepository.updatePassword(currentPassword, newPassword)
            }
        }
    }

    override fun onUpdateAddress() {
        val address = addressLiveData.value.toString().trim()
        checkThenExecute(
            validator = {  profileValidator.validateUpdateAddress(address) }
        ) {
            viewModelScope.launch {
                userRepository.updateAddress(address)
            }
        }
    }

    override fun onUpdatePhone() {
        val phoneNumber = phoneLiveData.value.toString().trim()
        checkThenExecute(
            validator = {  profileValidator.validateUpdatePhone(phoneNumber) }
        ) {
            viewModelScope.launch {
                userRepository.updatePhone(phoneNumber)
            }
        }
    }
}