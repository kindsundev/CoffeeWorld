package kind.sun.dev.coffeeworld.ui.more.user.profile.address

import android.text.TextUtils
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
class AddressDialogViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val networkHelper: NetworkHelper
): ViewModel() {
    val userUpdate: LiveData<NetworkResult<MessageResponse>>
        get() = userRepository.userUpdate

    val addressLiveData = MutableLiveData<String>("")
    val errorMessageLiveData = MutableLiveData<String>("")

    fun onUpdateAddress() {
        val address = addressLiveData.value.toString().trim()
        if (TextUtils.isEmpty(address)) {
            errorMessageLiveData.value = "Please provide your address"
        } else {
            updateAddress(address)
        }
    }

    private fun updateAddress(address: String) {
        viewModelScope.launch {
            if (networkHelper.isConnected) {
                userRepository.updateAddress(address)
            }
        }
    }
}