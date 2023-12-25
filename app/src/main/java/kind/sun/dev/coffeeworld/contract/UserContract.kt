package kind.sun.dev.coffeeworld.contract

import androidx.lifecycle.LiveData
import kind.sun.dev.coffeeworld.data.local.entity.UserEntity
import kind.sun.dev.coffeeworld.data.local.model.UserModel
import kind.sun.dev.coffeeworld.data.remote.response.MessageResponse
import kind.sun.dev.coffeeworld.data.remote.response.UserResponse
import kind.sun.dev.coffeeworld.util.api.NetworkResult
import java.io.File

interface UserContract {

    interface Validator {

        fun validateUpdateName(name: String): Pair<Boolean, String>

        fun validateUpdateAddress(address: String): Pair<Boolean, String>

        fun validatorUpdateEmail(email: String, password: String): Pair<Boolean, String>

        fun validateUpdatePhone(phoneNumber: String): Pair<Boolean, String>

        fun validateUpdatePassword(currentPassword: String, newPassword: String, reNewPassword: String): Pair<Boolean, String>
    }


    interface ViewModel {
        fun onFetchUser(isLoading: Boolean, onDataFromLocal: (UserModel?) -> Unit, onFailedMessage: (String) -> Unit)

        suspend fun onSyncUser(userModel: UserModel)

        fun onUpdateAvatar(avatar: File, onFailedMessage: (String) -> Unit)

        fun onUpdateName(onFailedMessage: (String) -> Unit)

        fun onUpdateEmail(onFailedMessage: (String) -> Unit)

        fun onUpdatePassword(onFailedMessage: (String) -> Unit)

        fun onUpdateAddress(onFailedMessage: (String) -> Unit)

        fun onUpdatePhone(onFailedMessage: (String) -> Unit)
    }


    interface Service {
        val userResponse: LiveData<NetworkResult<UserResponse>>
        val messageResponse: LiveData<NetworkResult<MessageResponse>>
        val username: String?

        suspend fun handleFetchUser(isLoading: Boolean)

        suspend fun handleSyncUser(userModel: UserModel)

        suspend fun handleGetUser(id: String): UserEntity?

        suspend fun handleUpdateAvatar(avatar: File)

        suspend fun handleUpdateEmail(email: String, password: String)

        suspend fun handleUpdateName(name: String)

        suspend fun handleUpdateAddress(address: String)

        suspend fun handleUpdatePhone(phone: String)

        suspend fun handleUpdatePassword(currentPassword: String, newPassword: String)
    }
}