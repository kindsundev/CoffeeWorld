package kind.sun.dev.coffeeworld.data.model.response.auth

data class LoginResponse(
    val success: Boolean,
    val data: LoginResult
)