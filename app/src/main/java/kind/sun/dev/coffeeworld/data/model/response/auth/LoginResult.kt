package kind.sun.dev.coffeeworld.data.model.response.auth

data class LoginResult(
    val token: String,
    val user: UserModel
)