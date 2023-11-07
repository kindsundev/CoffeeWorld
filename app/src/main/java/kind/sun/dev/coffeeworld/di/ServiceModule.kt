package kind.sun.dev.coffeeworld.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kind.sun.dev.coffeeworld.contract.AuthContract
import kind.sun.dev.coffeeworld.contract.CafeContract
import kind.sun.dev.coffeeworld.contract.UserContract
import kind.sun.dev.coffeeworld.data.local.dao.CafeDAO
import kind.sun.dev.coffeeworld.data.local.dao.UserDAO
import kind.sun.dev.coffeeworld.data.remote.api.AuthAPI
import kind.sun.dev.coffeeworld.data.remote.api.CafeAPI
import kind.sun.dev.coffeeworld.data.remote.api.UserAPI
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
    fun provideAuthService(remoteAPI: AuthAPI): AuthContract.Service {
        return AuthRepository(remoteAPI)
    }

    @Provides
    @Singleton
    fun provideUserService(
        remoteAPI: UserAPI, localDAO: UserDAO, preferencesHelper: PreferencesHelper
    ): UserContract.Service {
        return UserRepository(remoteAPI, localDAO, preferencesHelper)
    }

    @Provides
    @Singleton
    fun provideCafeService(remoteAPI: CafeAPI, localDAO: CafeDAO): CafeContract.Service {
        return CafeRepository(remoteAPI, localDAO)
    }
}