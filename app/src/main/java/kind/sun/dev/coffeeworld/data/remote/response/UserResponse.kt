package kind.sun.dev.coffeeworld.data.remote.response

import kind.sun.dev.coffeeworld.data.local.model.UserModel

data class UserResponse(
    val `data`: UserModel,
    val success: Boolean
)

