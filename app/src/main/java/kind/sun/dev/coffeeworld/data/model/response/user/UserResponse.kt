package kind.sun.dev.coffeeworld.data.model.response.user

import kind.sun.dev.coffeeworld.data.model.response.auth.UserModel

data class UserResponse(
    val `data`: UserModel,
    val success: Boolean
)