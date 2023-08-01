package kind.sun.dev.coffeeworld.data.model.request.user

import com.google.gson.annotations.SerializedName

data class UserPasswordRequest(
    val username: String,
    @SerializedName("old_password")
    val oldPassword: String,
    @SerializedName("new_password")
    val newPassword: String
)