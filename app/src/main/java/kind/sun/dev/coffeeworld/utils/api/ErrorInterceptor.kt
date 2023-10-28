package kind.sun.dev.coffeeworld.utils.api

import android.content.Context
import kind.sun.dev.coffeeworld.R
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.net.ssl.SSLException

class ErrorInterceptor @Inject constructor(
    private val context: Context
) : Interceptor {

    private lateinit var message: String

    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            chain.proceed(chain.request())
        } catch (e: SocketTimeoutException) {
            handleSocketTimeoutException(chain)
        } catch (e: SocketTimeoutException) {
            handleConnectionException(chain)
        } catch (e: SSLException) {
            handleSSLException(chain)
        } catch (e: IOException) {
            handleOtherException(chain)
        }
    }

    private fun handleSocketTimeoutException(chain: Interceptor.Chain): Response {
        message = context.getString(R.string.response_time_out)
        return createErrorResponse(chain, 408, message)
    }

    private fun handleConnectionException(chain: Interceptor.Chain): Response {
        message = context.getString(R.string.response_time_out)
        return createErrorResponse(chain, 503, message)
    }

    private fun handleSSLException(chain: Interceptor.Chain): Response {
        message = context.getString(R.string.response_default_error)
        return createErrorResponse(chain, 502, message)
    }

    private fun handleOtherException(chain: Interceptor.Chain): Response {
        message = context.getString(R.string.response_default_error)
        return createErrorResponse(chain, 500, message)
    }

    private fun createErrorResponse(
        chain: Interceptor.Chain,
        code: Int,
        message: String
    ): Response {
        val errorJSON = JSONObject().apply { put("message", message) }.toString()
        val responseBody = errorJSON.toResponseBody("application/json".toMediaTypeOrNull())

        return Response.Builder()
            .code(code)
            .message(message)
            .body(responseBody)
            .protocol(Protocol.HTTP_1_1)
            .request(chain.request())
            .build()
    }
}