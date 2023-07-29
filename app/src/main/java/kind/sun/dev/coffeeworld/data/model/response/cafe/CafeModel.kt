package kind.sun.dev.coffeeworld.data.model.response.cafe

import java.io.Serializable

data class CafeModel(
    val business_hours: String,
    val description: String,
    val id: Int,
    val image: String,
    val location: String,
    val name: String,
    val rating: Double
): Serializable