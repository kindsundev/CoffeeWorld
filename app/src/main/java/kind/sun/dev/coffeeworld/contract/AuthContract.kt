package kind.sun.dev.coffeeworld.contract

interface AuthContract {

    interface ViewModel {
        fun onLogin(onFailedMessage: (String) -> Unit)

        fun onRegister(onFailedMessage: (String) -> Unit)

        fun onPasswordReset(onFailedMessage: (String) -> Unit)
    }

    interface Validator {
        fun validateRegisterInput(
            name: String,
            email: String,
            username: String,
            password: String,
            confirmPassword: String
        ): Pair<Boolean, String>

        fun validateLoginInput(username: String, password: String): Pair<Boolean, String>

        fun validateForgotPasswordInput(username: String, email: String): Pair<Boolean, String>
    }
}