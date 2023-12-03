package kind.sun.dev.coffeeworld.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kind.sun.dev.coffeeworld.data.local.entity.CafeEntity

@Dao
interface CafeDao {
    @Query("SELECT * FROM cafe_table WHERE id = :id")
    suspend fun getCafe(id: String): CafeEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCafe(cafe: CafeEntity)

}