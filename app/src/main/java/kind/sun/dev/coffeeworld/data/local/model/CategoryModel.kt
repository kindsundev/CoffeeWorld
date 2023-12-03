package kind.sun.dev.coffeeworld.data.local.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "category_table")
data class CategoryModel(
    val id: Int,
    @SerializedName("cafe_id")
    val cafeId: Int,
    val name: String,
    val image: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CategoryModel

        if (id != other.id) return false
        if (cafeId != other.cafeId) return false
        if (name != other.name) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + cafeId
        result = 31 * result + name.hashCode()
        result = 31 * result + (image?.contentHashCode() ?: 0)
        return result
    }
}