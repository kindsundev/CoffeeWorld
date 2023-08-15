package kind.sun.dev.coffeeworld.data.model.response.cafe

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CafeModel(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val location: String,
    val rating: Double,
    @SerializedName("business_hours")
    val businessHours: String
): Serializable