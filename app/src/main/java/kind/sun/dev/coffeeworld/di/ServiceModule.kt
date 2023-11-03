package kind.sun.dev.coffeeworld.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kind.sun.dev.coffeeworld.api.AuthAPI
import kind.sun.dev.coffeeworld.api.CafeAPI
import kind.sun.dev.coffeeworld.api.UserAPI
import kind.sun.dev.coffeeworld.contract.AuthContract
import kind.sun.dev.coffeeworld.contract.CafeContract
import kind.sun.dev.coffeeworld.contract.UserContract
import kind.sun.dev.coffeeworld.data.repository.AuthRepository
import kind.sun.dev.coffeeworld.data.repository.CafeRepository
import kind.sun.dev.coffeeworld.data.repository.UserRepository
import kind.sun.dev.coffeeworld.utils.helper.storage.PreferencesHelper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideAuthService(authAPI: AuthAPI): AuthContract.Service = AuthRepository(authAPI)

    @Provides
    @Singleton
    fun provideUserService(userAPI: UserAPI, preferences: PreferencesHelper): UserContract.Service {
        return UserRepository(userAPI, preferences)
    }

    @Provides
    @Singleton
    fun provideCafeService(cafeAPI: CafeAPI): CafeContract.Service = CafeRepository(cafeAPI)
}