package kind.sun.dev.coffeeworld.data.remote.request

import com.google.gson.annotations.SerializedName

data class UserEmailRequest (
    val username: String,
    val email: String,
    val password: String
)

data class UserPasswordRequest(
    val username: String,
    @SerializedName("old_password")
    val oldPassword: String,
    @SerializedName("new_password")
    val newPassword: String
)