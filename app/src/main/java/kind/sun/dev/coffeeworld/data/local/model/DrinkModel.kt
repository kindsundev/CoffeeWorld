package kind.sun.dev.coffeeworld.data.local.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "drink_table")
data class DrinkModel(
    val id: Int,
    @SerializedName("cafe_id")
    val cafeId: Int,
    val name: String,
    val quantity: Int,
    val price: Double,
    val description: String,
    val image: String,
    @SerializedName("category_id")
    val categoryId: Int,
    val rating: Double
)