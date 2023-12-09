package kind.sun.dev.coffeeworld.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kind.sun.dev.coffeeworld.data.local.model.BeverageCategoryModel
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.data.local.model.UserModel

@Entity(tableName = "cafe_table")
data class CafeEntity(
    @PrimaryKey
    val id: String,
    val items: List<CafeModel>
)

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val detail: UserModel
)

@Entity(tableName = "menu_table")
data class MenuEntity(
    @PrimaryKey
    @ColumnInfo(name = "cafe_id")
    val cafeId: Int,
    @ColumnInfo(name = "beverage_categories")
    val beverageCategories: List<BeverageCategoryModel>
)