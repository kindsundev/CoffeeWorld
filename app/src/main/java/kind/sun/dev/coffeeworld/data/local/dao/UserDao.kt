package kind.sun.dev.coffeeworld.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kind.sun.dev.coffeeworld.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user_table WHERE id = :id")
    fun getUser(id: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUser(user: UserEntity)

}