package kind.sun.dev.coffeeworld.data.model.request.user

data class  UserEmailRequest (
    val username: String,
    val email: String,
    val password: String
)