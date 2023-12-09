package kind.sun.dev.coffeeworld.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kind.sun.dev.coffeeworld.data.local.entity.CafeEntity
import kind.sun.dev.coffeeworld.data.local.entity.MenuEntity

@Dao
interface CafeDao {
    @Query("SELECT * FROM cafe_table WHERE id = :id")
    suspend fun getCafe(id: String): CafeEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCafe(cafe: CafeEntity)

    @Query("SELECT * FROM menu_table WHERE cafe_id = :cafeId")
    suspend fun getMenu(cafeId: Int): MenuEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMenu(menu: MenuEntity)
}