package kind.sun.dev.coffeeworld.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kind.sun.dev.coffeeworld.data.local.dao.CafeDao
import kind.sun.dev.coffeeworld.data.local.dao.UserDao
import kind.sun.dev.coffeeworld.data.local.entity.CafeEntity
import kind.sun.dev.coffeeworld.data.local.entity.MenuEntity
import kind.sun.dev.coffeeworld.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, CafeEntity::class, MenuEntity::class],
    exportSchema = true,
    version = 1
)
@TypeConverters(AppConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun cafeDao(): CafeDao

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