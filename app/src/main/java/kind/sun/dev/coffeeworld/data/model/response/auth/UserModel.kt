package kind.sun.dev.coffeeworld.data.model.response.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val id: Int,
    val username: String,
    val name: String,
    val address: String?,
    val email: String,
    val image: ByteArray,
    val phone: String?
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserModel

        if (address != other.address) return false
        if (email != other.email) return false
        if (id != other.id) return false
        if (!image.contentEquals(other.image)) return false
        if (name != other.name) return false
        if (phone != other.phone) return false
        if (username != other.username) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + username.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (address?.hashCode() ?: 0)
        result = 31 * result + email.hashCode()
        result = 31 * result + image.contentHashCode()
        result = 31 * result + (phone?.hashCode() ?: 0)
        return result
    }

}