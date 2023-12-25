package kind.sun.dev.coffeeworld.util.validator

import android.text.TextUtils
import android.util.Patterns
import kind.sun.dev.coffeeworld.contract.AuthContract
import javax.inject.Inject

class AuthValidator @Inject constructor() : AuthContract.Validator {

    override fun validateRegisterInput(
        name: String,
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): Pair<Boolean, String> {
        return when {
            TextUtils.isEmpty(username) || TextUtils.isEmpty(password)
                    || TextUtils.isEmpty(email) || TextUtils.isEmpty(name) -> {
                Pair(false, "Please provide all the credentials")
            }
            username.contains("\\s".toRegex()) -> {
                Pair(false, "Username must not have spaces")
            }
            username.endsWith(".") -> {
                Pair(false, "Username cannot end with dot")
            }
            password.length < 8 -> {
                Pair(false, "Password must be more than 8 characters")
            }
            password != confirmPassword -> {
                Pair(false, "Password confirmation does not match")
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Pair(false, "Please provide a valid email")
            }
            TextUtils.isDigitsOnly(name) -> {
                Pair(false, "Name cannot be a number")
            }
            else -> Pair(true, "")
        }
    }

    override fun validateLoginInput(username: String, password: String): Pair<Boolean, String> {
        return when {
            TextUtils.isEmpty(username) || TextUtils.isEmpty(password) -> {
                 Pair(false, "Please provide all the credentials")
            }
            username.contains("\\s".toRegex()) -> {
                 Pair(false, "Username must not have spaces")
            }
            username.endsWith(".") -> {
                 Pair(false, "Username cannot end with dot")
            }
            password.length < 8 -> {
                 Pair(false, "Password must be more than 8 characters")
            }
            else -> Pair(true, "")
        }
    }

    override fun validateForgotPasswordInput(
        username: String,
        email: String
    ): Pair<Boolean, String> {
        return when {
            TextUtils.isEmpty(username) || TextUtils.isEmpty(email) -> {
                Pair(false, "Please provide all the credentials")
            }
            username.contains("\\s".toRegex()) -> {
                Pair(false, "Username must not have spaces")
            }
            username.endsWith(".") -> {
                Pair(false, "Username cannot end with dot")
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Pair(false, "Please provide a valid email")
            }
            else -> Pair(true, "")
        }
    }

}