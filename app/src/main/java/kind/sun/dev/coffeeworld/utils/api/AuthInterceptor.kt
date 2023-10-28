package kind.sun.dev.coffeeworld.utils.api

import kind.sun.dev.coffeeworld.utils.helper.storage.PreferencesHelper
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {

    @Inject
    lateinit var preferences: PreferencesHelper

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${preferences.userToken}")
                .build()
        )
    }

}