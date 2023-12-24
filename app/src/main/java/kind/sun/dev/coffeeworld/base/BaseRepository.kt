package kind.sun.dev.coffeeworld.base

import androidx.lifecycle.MutableLiveData
import kind.sun.dev.coffeeworld.data.remote.response.MessageResponse
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.common.Logger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

open class BaseRepository {

    private val baseExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Logger.error(message = "BaseExceptionHandler: ${throwable.message}")
    }

    protected val coroutineScope by lazy { CoroutineScope(Dispatchers.IO) }

    protected val statusMessage by lazy { MutableLiveData<NetworkResult<MessageResponse>>() }

    protected fun convertToTextRequestBody(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaType())
    }

    protected suspend fun <T> performNetworkOperation(
        isShowProgress: Boolean = true,
        networkRequest: suspend () -> Response<T>,
        networkResult: MutableLiveData<NetworkResult<T>>
    ) {
        if (isShowProgress) networkResult.postValue(NetworkResult.Loading())
        withContext(Dispatchers.IO + baseExceptionHandler) {
            supervisorScope {
                withContext(Dispatchers.Main) {
                    handleNetworkResponse(networkRequest(), networkResult)
                }
            }
        }
    }

    private fun <T> handleNetworkResponse(
        networkResponse: Response<T>?,
        networkObserver: MutableLiveData<NetworkResult<T>>
    ) {
        if (networkResponse == null) {
            networkObserver.postValue(NetworkResult.Error("Please login again to continue using this feature"))
            return
        }
        if (networkResponse.isSuccessful && networkResponse.body() != null) {
            networkObserver.postValue(NetworkResult.Success(networkResponse.body()!!))
            return
        }
        val errorMessage = networkResponse.errorBody()?.parseErrorMessage() ?: "Something went wrong"
        networkObserver.postValue(NetworkResult.Error(errorMessage))
    }

    private fun ResponseBody.parseErrorMessage(): String {
        return try {
            JSONObject(string()).getString("message")
        } catch (e: JSONException) {
            Logger.error(message = "JSONException: ${e.message.toString()}")
            "Something went wrong"
        }
    }
}