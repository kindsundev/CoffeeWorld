package kind.sun.dev.coffeeworld.data.local.model

import com.google.gson.annotations.SerializedName

class BeverageCategoryModel(
    val id: String,
    @SerializedName("cafe_id")
    val cafeId: Int,
    val category: CategoryModel,
    val drinks: List<DrinkModel>
)