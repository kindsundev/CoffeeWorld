package kind.sun.dev.coffeeworld.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.base.BaseViewModel
import kind.sun.dev.coffeeworld.contract.UserContract
import kind.sun.dev.coffeeworld.data.local.model.UserModel
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.validator.ProfileValidator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userService: UserContract.Service,
    private val profileValidator: ProfileValidator,
) : BaseViewModel(), UserContract.ViewModel {

    val nameLiveData by lazy { MutableLiveData<String>() }
    val addressLiveData by lazy { MutableLiveData<String>() }
    val phoneLiveData by lazy { MutableLiveData<String>() }
    val emailLiveData by lazy { MutableLiveData<String>() }
    val emailPasswordLiveData by lazy { MutableLiveData<String>() }
    val currentPasswordLiveData by lazy { MutableLiveData<String>() }
    val newPasswordLiveData by lazy { MutableLiveData<String>() }
    val retypeNewPasswordLiveData by lazy { MutableLiveData<String>() }

    val userResponse get() = userService.userResponse
    val messageResponse get() = userService.messageResponse

    override fun onFetchUser(
        isLoading: Boolean,
        onDataFromLocal: (UserModel?) -> Unit,
        onFailedMessage: (String) -> Unit
    ) {
        handleCheckAndRoute(
            conditionChecker = null,
            onPassedCheck = {
                viewModelScope.launch { userService.handleFetchUser(isLoading) }
            },
            onFailedCheck = { reason, localDataRequired ->
                if (localDataRequired) {
                    viewModelScope.launch {
                        userService.handleGetUser(Constants.DB_USER_KEY)?.let {
                            onDataFromLocal(it.detail)
                            delay(300)
                            onFailedMessage(reason)
                        }
                    }
                } else {
                    onFailedMessage(reason)
                }
            }
        )
    }

    override suspend fun onSyncUser(userModel: UserModel) = userService.handleSyncUser(userModel)

    override fun onUpdateAvatar(avatar: File, onFailedMessage: (String) -> Unit) {
        handleCheckAndRoute(
            conditionChecker = null,
            onPassedCheck = { viewModelScope.launch { userService.handleUpdateAvatar(avatar) } },
            onFailedCheck = { reason, _ -> onFailedMessage(reason) }
        )
    }

    override fun onUpdateName(onFailedMessage: (String) -> Unit) {
        val name = nameLiveData.value.toString().trim()
        handleCheckAndRoute(
            conditionChecker = { profileValidator.validateUpdateName(name) },
            onPassedCheck = { viewModelScope.launch { userService.handleUpdateName(name) } },
            onFailedCheck = { reason, _ -> onFailedMessage(reason) }
        )
    }

    override fun onUpdateEmail(onFailedMessage: (String) -> Unit) {
        val email = emailLiveData.value.toString().trim()
        val password = emailPasswordLiveData.value.toString().trim()
        handleCheckAndRoute(
            conditionChecker = { profileValidator.validatorUpdateEmail(email, password) },
            onPassedCheck = { viewModelScope.launch { userService.handleUpdateEmail(email, password) } },
            onFailedCheck = { reason, _ -> onFailedMessage(reason) }
        )
    }

    override fun onUpdatePassword(onFailedMessage: (String) -> Unit) {
        val currentPassword = currentPasswordLiveData.value.toString().trim()
        val newPassword = newPasswordLiveData.value.toString().trim()
        val reNewPassword = retypeNewPasswordLiveData.value.toString().trim()
        handleCheckAndRoute(
            conditionChecker = {
                profileValidator.validateUpdatePassword(currentPassword, newPassword, reNewPassword)
            },
            onPassedCheck = {
                viewModelScope.launch { userService.handleUpdatePassword(currentPassword, newPassword) }
            },
            onFailedCheck = { reason, _ -> onFailedMessage(reason)}
        )
    }

    override fun onUpdateAddress(onFailedMessage: (String) -> Unit) {
        val address = addressLiveData.value.toString().trim()
        handleCheckAndRoute(
            conditionChecker = { profileValidator.validateUpdateAddress(address) },
            onPassedCheck = { viewModelScope.launch { userService.handleUpdateAddress(address) } },
            onFailedCheck = { reason, _ -> onFailedMessage(reason) }
        )
    }

    override fun onUpdatePhone(onFailedMessage: (String) -> Unit) {
        val phoneNumber = phoneLiveData.value.toString().trim()
        handleCheckAndRoute(
            conditionChecker = { profileValidator.validateUpdatePhone(phoneNumber) },
            onPassedCheck = { viewModelScope.launch { userService.handleUpdatePhone(phoneNumber) } },
            onFailedCheck = { reason, _ -> onFailedMessage(reason) }
        )
    }

}