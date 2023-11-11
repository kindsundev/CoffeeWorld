package kind.sun.dev.coffeeworld.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kind.sun.dev.coffeeworld.contract.AuthContract
import kind.sun.dev.coffeeworld.contract.CafeContract
import kind.sun.dev.coffeeworld.contract.UserContract
import kind.sun.dev.coffeeworld.data.local.dao.CafeDao
import kind.sun.dev.coffeeworld.data.local.dao.UserDao
import kind.sun.dev.coffeeworld.data.remote.api.AuthApi
import kind.sun.dev.coffeeworld.data.remote.api.CafeApi
import kind.sun.dev.coffeeworld.data.remote.api.UserApi
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
    fun provideAuthService(remoteApi: AuthApi): AuthContract.Service {
        return AuthRepository(remoteApi)
    }

    @Provides
    @Singleton
    fun provideUserService(
        remoteApi: UserApi, localDao: UserDao, preferencesHelper: PreferencesHelper
    ): UserContract.Service {
        return UserRepository(remoteApi, localDao, preferencesHelper)
    }

    @Provides
    @Singleton
    fun provideCafeService(remoteApi: CafeApi, localDao: CafeDao): CafeContract.Service {
        return CafeRepository(remoteApi, localDao)
    }
}