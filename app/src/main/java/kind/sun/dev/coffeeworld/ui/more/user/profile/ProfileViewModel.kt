package kind.sun.dev.coffeeworld.ui.more.user.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.data.model.response.user.UserResponse
import kind.sun.dev.coffeeworld.data.repository.UserRepository
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.network.NetworkHelper
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val networkHelper: NetworkHelper
): ViewModel(){
    val userResponseLiveData : LiveData<NetworkResult<UserResponse>>
        get() = userRepository.user

    fun getUser() {
        viewModelScope.launch {
            if (networkHelper.isConnected) {
                userRepository.getUser()
            }
        }
    }

}