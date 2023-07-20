package kind.sun.dev.coffeeworld.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import kind.sun.dev.coffeeworld.data.api.AuthService
import kind.sun.dev.coffeeworld.utils.AuthInterceptor
import kind.sun.dev.coffeeworld.utils.Constants.API_BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(API_BASE_URL)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @ActivityScoped
    @Provides
    fun provideAuthService(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): AuthService {
        return retrofitBuilder.client(okHttpClient).build().create(AuthService::class.java)
    }

}