package kind.sun.dev.coffeeworld.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
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