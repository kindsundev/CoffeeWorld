package kind.sun.dev.coffeeworld.ui.more.user.profile

import java.io.File

interface ProfileContract {

    interface Service {
        fun getUser()

        fun onUpdateAvatar(avatar: File)

        fun onUpdateName()

        fun onUpdateEmail()

        fun onUpdatePassword()

        fun onUpdateAddress()

        fun onUpdatePhone()
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