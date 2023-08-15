package kind.sun.dev.coffeeworld.data.model.response.cafe

import com.google.gson.annotations.SerializedName

data class CategoryModel(
    val id: Int,
    @SerializedName("cafe_id")
    val cafeId: Int,
    val name: String
)