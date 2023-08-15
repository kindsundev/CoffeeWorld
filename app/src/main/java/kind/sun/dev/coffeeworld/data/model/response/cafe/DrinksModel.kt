package kind.sun.dev.coffeeworld.data.model.response.cafe

import com.google.gson.annotations.SerializedName

data class DrinksModel(
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