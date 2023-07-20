package kind.sun.dev.coffeeworld.data.model.response.auth

data class UserModel(
    val address: String,
    val email: String,
    val id: Int,
    val image: String,
    val name: String,
    val phone: String,
    val username: String
)