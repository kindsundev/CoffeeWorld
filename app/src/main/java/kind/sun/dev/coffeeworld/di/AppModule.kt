package kind.sun.dev.coffeeworld.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kind.sun.dev.coffeeworld.data.local.dao.CafeDao
import kind.sun.dev.coffeeworld.data.local.dao.UserDao
import kind.sun.dev.coffeeworld.data.local.db.AppDatabase
import kind.sun.dev.coffeeworld.util.helper.storage.PreferencesHelper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePreferencesHelper(@ApplicationContext context: Context): PreferencesHelper {
        return PreferencesHelper(context)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) = AppDatabase(context)

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    @Provides
    @Singleton
    fun provideCafeDao(database: AppDatabase): CafeDao = database.cafeDao()
}