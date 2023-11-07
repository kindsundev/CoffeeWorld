package kind.sun.dev.coffeeworld.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kind.sun.dev.coffeeworld.data.local.dao.CafeDAO
import kind.sun.dev.coffeeworld.data.local.dao.UserDAO
import kind.sun.dev.coffeeworld.data.local.db.AppDatabase
import kind.sun.dev.coffeeworld.utils.helper.storage.PreferencesHelper
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
    fun provideUserDao(database: AppDatabase): UserDAO = database.userDAO()

    @Provides
    @Singleton
    fun provideCafeDao(database: AppDatabase): CafeDAO = database.cafeDAO()
}