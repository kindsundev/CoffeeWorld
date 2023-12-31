package kind.sun.dev.coffeeworld.data.local.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class MenuModel(
    @PrimaryKey
    val id: String,
    @SerializedName("cafe_id")
    val cafeId: Int,
    @SerializedName("beverage_category")
    val beverageCategories: List<BeverageCategoryModel>
)