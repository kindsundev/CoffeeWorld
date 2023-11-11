package kind.sun.dev.coffeeworld.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kind.sun.dev.coffeeworld.data.local.model.CafeModel

@Dao
interface CafeDao {
    @Query("SELECT * FROM cafe_table")
    suspend fun getAllCafes(): List<CafeModel>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCafe(cafe: CafeModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAllCafes(cafes: List<CafeModel>)

    @Delete
    suspend fun deleteCafe(cafe: CafeModel)

    @Query("DELETE FROM cafe_table")
    suspend fun deleteAllCafes()
}