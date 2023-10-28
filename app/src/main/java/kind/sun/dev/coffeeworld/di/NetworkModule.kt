package kind.sun.dev.coffeeworld.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kind.sun.dev.coffeeworld.BuildConfig
import kind.sun.dev.coffeeworld.api.AuthService
import kind.sun.dev.coffeeworld.api.CafeService
import kind.sun.dev.coffeeworld.api.UserService
import kind.sun.dev.coffeeworld.utils.api.AuthInterceptor
import kind.sun.dev.coffeeworld.utils.api.ErrorInterceptor
import kind.sun.dev.coffeeworld.utils.custom.WithAuth
import kind.sun.dev.coffeeworld.utils.custom.WithoutAuth
import kind.sun.dev.coffeeworld.utils.helper.network.NetworkReceiverHelper
import kind.sun.dev.coffeeworld.utils.helper.network.NetworkHelper
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
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
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
    fun provideAuthService(
        retrofitBuilder: Retrofit.Builder,
        @WithAuth okHttpClient: OkHttpClient
    ): AuthService {
        return retrofitBuilder.client(okHttpClient).build().create(AuthService::class.java)
    }

    @Singleton
    @Provides
    fun provideCafeService(
        retrofitBuilder: Retrofit.Builder,
        @WithoutAuth okHttpClient: OkHttpClient
    ): CafeService {
        return retrofitBuilder.client(okHttpClient).build().create(CafeService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserService(
        retrofitBuilder: Retrofit.Builder,
        @WithAuth okHttpClient: OkHttpClient
    ): UserService {
        return retrofitBuilder.client(okHttpClient).build().create(UserService::class.java)
    }
}