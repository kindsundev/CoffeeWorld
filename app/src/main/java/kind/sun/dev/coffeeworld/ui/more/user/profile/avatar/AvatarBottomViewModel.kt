package kind.sun.dev.coffeeworld.ui.more.user.profile.avatar

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.data.model.response.user.UserUpdateResponse
import kind.sun.dev.coffeeworld.data.repository.UserRepository
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.network.NetworkHelper
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AvatarBottomViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val networkHelper: NetworkHelper
): ViewModel() {

    val userUpdateResponseLiveData: LiveData<NetworkResult<UserUpdateResponse>>
        get() = userRepository.userUpdateResponseLiveData


    fun updateAvatar(avatar: File) {
        viewModelScope.launch {
            if (networkHelper.isConnected) {
                userRepository.updateAvatar(avatar)
            }
        }
    }
}