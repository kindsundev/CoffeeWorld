package kind.sun.dev.coffeeworld.data.model.request

data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String,
    val name: String
)