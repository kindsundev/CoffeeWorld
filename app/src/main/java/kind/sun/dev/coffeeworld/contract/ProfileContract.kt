package kind.sun.dev.coffeeworld.contract

import kind.sun.dev.coffeeworld.data.local.model.UserModel
import java.io.File

interface ProfileContract {

    interface ViewModel {
        fun onFetchUser(
            onDataFromLocal: (UserModel?) -> Unit,
            onFailedMessage: (String) -> Unit
        )

        suspend fun onSyncUser(userModel: UserModel): Long

        fun onUpdateAvatar(avatar: File, onFailedMessage: (String) -> Unit)

        fun onUpdateName(onFailedMessage: (String) -> Unit)

        fun onUpdateEmail(onFailedMessage: (String) -> Unit)

        fun onUpdatePassword(onFailedMessage: (String) -> Unit)

        fun onUpdateAddress(onFailedMessage: (String) -> Unit)

        fun onUpdatePhone(onFailedMessage: (String) -> Unit)
    }

    interface Validator {

        fun validateUpdateName(name: String): Pair<Boolean, String>

        fun validateUpdateAddress(address: String): Pair<Boolean, String>

        fun validatorUpdateEmail(email: String, password: String): Pair<Boolean, String>

        fun validateUpdatePhone(phoneNumber: String): Pair<Boolean, String>

        fun validateUpdatePassword(
            currentPassword: String,
            newPassword: String,
            reNewPassword: String
        ): Pair<Boolean, String>
    }


}