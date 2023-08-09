package kind.sun.dev.coffeeworld.ui.more.user.profile.name

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
class NameDialogViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val networkHelper: NetworkHelper
): ViewModel() {
    val userUpdateResponseLiveData: LiveData<NetworkResult<UserUpdateResponse>>
        get() = userRepository.userUpdateResponseLiveData

    val nameLiveData = MutableLiveData<String>("")
    val errorMessageLiveData = MutableLiveData<String>("")

    fun onUpdateName() {
        val name = nameLiveData.value.toString().trim()
        if (TextUtils.isEmpty(name)) {
            errorMessageLiveData.value = "Please provide your name"
        } else {
            updateName(name)
        }
    }

    private fun updateName(name: String) {
        viewModelScope.launch {
            if (networkHelper.isConnected) {
                userRepository.updateName(name)
            }
        }
    }

}