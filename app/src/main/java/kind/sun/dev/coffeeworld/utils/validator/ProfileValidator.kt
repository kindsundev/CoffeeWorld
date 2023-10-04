package kind.sun.dev.coffeeworld.utils.validator

import android.text.TextUtils
import android.util.Patterns
import kind.sun.dev.coffeeworld.contract.ProfileContract
import javax.inject.Inject

class ProfileValidator @Inject constructor() : ProfileContract.Validator {

    override fun validateUpdateName(name: String): Pair<Boolean, String> {
        return when {
            TextUtils.isEmpty(name) -> {
                Pair(false, "Please provide your name")
            }
            name.length > 100 -> {
                Pair(false, "Invalid name because it is too long")
            }
            else -> Pair(true, "")
        }
    }

    override fun validateUpdateAddress(address: String): Pair<Boolean, String> {
        return when {
            TextUtils.isEmpty(address) -> {
                Pair(false, "Please provide your address")
            }
            address.length < 5 -> {
                Pair(false, "You need to enter your full address")
            }
            else -> Pair(true, "")
        }
    }

    override fun validatorUpdateEmail(email: String, password: String): Pair<Boolean, String> {
        return when {
            TextUtils.isEmpty(email) -> {
                Pair(false, "Please provide your email")
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Pair(false, "Please provide a valid email")
            }
            password.length < 8 -> {
                Pair(false, "Password must be more than 8 characters")
            }
            else -> Pair(true, "")
        }
    }

    override fun validateUpdatePhone(phoneNumber: String): Pair<Boolean, String> {
        val regexPattern = "^([+0])(?!.*\\D).*"
        return when {
            TextUtils.isEmpty(phoneNumber) -> {
                Pair(false, "Please provide your phone number")
            }
            !Regex(regexPattern).matches(phoneNumber) -> {
                Pair(false, "Invalid phone number")
            }
            phoneNumber.length < 10 -> {
                Pair(false, "Phone number must be more than 10 characters")
            }
            else -> Pair(true, "")
        }
    }

    override fun validateUpdatePassword(
        currentPassword: String,
        newPassword: String,
        reNewPassword: String
    ): Pair<Boolean, String> {
        return when {
            currentPassword.length < 8 || newPassword.length < 8 || reNewPassword.length < 8 -> {
                 Pair(false, "Password must be more than 8 characters")
            }
            newPassword != reNewPassword -> {
                 Pair(false, "Both new passwords don't match")
            }
            else -> Pair(true, "")
        }
    }

}