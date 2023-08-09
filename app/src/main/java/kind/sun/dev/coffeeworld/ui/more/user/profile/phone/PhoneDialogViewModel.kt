package kind.sun.dev.coffeeworld.ui.more.user.profile.phone

import android.text.TextUtils
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
class PhoneDialogViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val networkHelper: NetworkHelper
): ViewModel() {
    val userUpdateResponseLiveData: LiveData<NetworkResult<UserUpdateResponse>>
        get() = userRepository.userUpdateResponseLiveData

    val phoneLiveData = MutableLiveData<String>("")
    val errorMessageLiveData = MutableLiveData<String?>("")
    private val regexPattern = "^([+0])(?!.*\\D).*"

    fun onUpdatePhone() {
        val phoneNumber = phoneLiveData.value.toString().trim()
        val validationResult = validateDataInput(phoneNumber)
        if (validationResult.first) {
            updatePhone(phoneNumber)
        } else {
            errorMessageLiveData.value = validationResult.second
        }
    }

    private fun validateDataInput(phoneNumber: String): Pair<Boolean, String> {
        var result = Pair(true, "")
        when {
            TextUtils.isEmpty(phoneNumber) -> {
                result = Pair(false, "Please provide your phone number")
            }
            !Regex(regexPattern).matches(phoneNumber) -> {
                result = Pair(false, "Invalid phone number")
            }
            phoneNumber.length < 10 -> {
                result = Pair(false, "Phone number must be more than 10 characters")
            }
        }
        return result
    }

    private fun updatePhone(phoneNumber: String) {
        viewModelScope.launch {
            if (networkHelper.isConnected) {
                userRepository.updatePhone(phoneNumber)
            }
        }
    }
}