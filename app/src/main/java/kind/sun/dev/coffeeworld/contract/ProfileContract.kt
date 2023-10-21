package kind.sun.dev.coffeeworld.contract

import kind.sun.dev.coffeeworld.data.local.model.UserModel
import java.io.File

interface ProfileContract {

    interface ViewModel {
        fun onFetchUser(onDataFromLocal: (UserModel?) -> Unit)

        fun onUpdateAvatar(avatar: File, message: (String) -> Unit)

        fun onUpdateName(message: (String) -> Unit)

        fun onUpdateEmail(message: (String) -> Unit)

        fun onUpdatePassword(message: (String) -> Unit)

        fun onUpdateAddress(message: (String) -> Unit)

        fun onUpdatePhone(message: (String) -> Unit)
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