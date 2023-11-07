package kind.sun.dev.coffeeworld.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kind.sun.dev.coffeeworld.data.local.dao.CafeDAO
import kind.sun.dev.coffeeworld.data.local.dao.UserDAO
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.data.local.model.UserModel

@Database(
    entities = [UserModel::class, CafeModel::class],
    exportSchema = true,
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDAO(): UserDAO
    abstract fun cafeDAO(): CafeDAO

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private const val name = "app_db.db"
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, name)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}