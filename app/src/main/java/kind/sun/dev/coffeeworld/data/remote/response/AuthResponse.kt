package kind.sun.dev.coffeeworld.data.remote.response

import kind.sun.dev.coffeeworld.data.local.model.UserModel


data class LoginResponse(
    val success: Boolean,
    val data: LoginResult
)

data class LoginResult(
    val token: String,
    val user: UserModel
)

data class MessageResponse(
    val `data`: String,
    val success: Boolean
)