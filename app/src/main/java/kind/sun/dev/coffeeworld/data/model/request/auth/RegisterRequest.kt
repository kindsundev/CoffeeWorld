package kind.sun.dev.coffeeworld.data.model.request.auth

data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String,
    val name: String
)