package kind.sun.dev.coffeeworld.utils

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import java.net.SocketTimeoutException
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {

    @Inject
    lateinit var tokenManager: TokenManager

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val token = tokenManager.getToken()
        request.addHeader("Authorization", "Bearer $token")
        val originalRequest = request.build()

        return try {
            return chain.proceed(originalRequest)
        } catch (e: SocketTimeoutException) {
            handleTimeoutException(chain,)
        }
    }

    private fun handleTimeoutException(chain: Interceptor.Chain): Response {
        val message = "Request timeout, please try again later"
        val errorJSON = JSONObject().apply {
            put("message", message)
        }
        val mediaType = "application/json".toMediaTypeOrNull()
        val responseBody = errorJSON.toString().toResponseBody(mediaType)

        return Response.Builder()
            .code(408)
            .message(message)
            .body(responseBody)
            .protocol(Protocol.HTTP_1_1)
            .request(chain.request())
            .build()
    }


}