package kind.sun.dev.coffeeworld.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kind.sun.dev.coffeeworld.data.local.model.UserModel

@Dao
interface UserDAO {

    @Query("SELECT * FROM user_table LIMIT 1")
    fun getUser(): UserModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUser(user: UserModel): Long

    @Delete
    suspend fun deleteUser(user: UserModel)
}