package kind.sun.dev.coffeeworld.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kind.sun.dev.coffeeworld.BuildConfig
import kind.sun.dev.coffeeworld.data.remote.api.AuthApi
import kind.sun.dev.coffeeworld.data.remote.api.CafeApi
import kind.sun.dev.coffeeworld.data.remote.api.UserApi
import kind.sun.dev.coffeeworld.util.api.AuthInterceptor
import kind.sun.dev.coffeeworld.util.api.ErrorInterceptor
import kind.sun.dev.coffeeworld.util.common.Logger
import kind.sun.dev.coffeeworld.util.custom.WithAuth
import kind.sun.dev.coffeeworld.util.custom.WithoutAuth
import kind.sun.dev.coffeeworld.util.helper.network.NetworkHelper
import kind.sun.dev.coffeeworld.util.helper.network.NetworkReceiverHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideNetworkStateManager(@ApplicationContext context: Context): NetworkReceiverHelper {
        return NetworkReceiverHelper(context)
    }

    @Singleton
    @Provides
    fun provideNetworkHelper(@ApplicationContext context: Context): NetworkHelper {
        return NetworkHelper(context)
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.API_BASE_URL)
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            Logger.debug(Logger.API, message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }

    @Provides
    @Singleton
    fun provideErrorInterceptor(@ApplicationContext context: Context): ErrorInterceptor {
        return ErrorInterceptor(context)
    }

    @Singleton
    @Provides
    @WithAuth
    fun provideOkHttpClientWithAuth(
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        errorInterceptor: ErrorInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor(errorInterceptor)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    @WithoutAuth
    fun provideOkHttpClientWithoutAuth(
        loggingInterceptor: HttpLoggingInterceptor,
        errorInterceptor: ErrorInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(errorInterceptor)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthAPI(
        retrofitBuilder: Retrofit.Builder,
        @WithAuth okHttpClient: OkHttpClient
    ): AuthApi {
        return retrofitBuilder.client(okHttpClient).build().create(AuthApi::class.java)
    }

    @Singleton
    @Provides
    fun provideCafeAPI(
        retrofitBuilder: Retrofit.Builder,
        @WithoutAuth okHttpClient: OkHttpClient
    ): CafeApi {
        return retrofitBuilder.client(okHttpClient).build().create(CafeApi::class.java)
    }

    @Singleton
    @Provides
    fun provideUserAPI(
        retrofitBuilder: Retrofit.Builder,
        @WithAuth okHttpClient: OkHttpClient
    ): UserApi {
        return retrofitBuilder.client(okHttpClient).build().create(UserApi::class.java)
    }
}