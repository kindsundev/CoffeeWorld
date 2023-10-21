package kind.sun.dev.coffeeworld.base

import androidx.lifecycle.MutableLiveData
import kind.sun.dev.coffeeworld.data.remote.response.MessageResponse
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.common.Logger
import kotlinx.coroutines.CoroutineExceptionHandler
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

    protected val statusMessage by lazy { MutableLiveData<NetworkResult<MessageResponse>>() }

    private val baseExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Logger.error("BaseExceptionHandler: ${throwable.message}")
    }

    protected fun convertToTextRequestBody(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaType())
    }

    protected suspend fun <T> performNetworkOperation(
        liveData: MutableLiveData<NetworkResult<T>>,
        operation: suspend () -> Response<T>
    ) {
        liveData.postValue(NetworkResult.Loading())
        withContext(Dispatchers.IO + baseExceptionHandler) {
            supervisorScope {
                withContext(Dispatchers.Main) {
                    handleNetworkResponse(operation(), liveData)
                }
            }
        }
    }

    private fun <T> handleNetworkResponse(
        response: Response<T>?,
        liveData: MutableLiveData<NetworkResult<T>>
    ) {
        if (response == null) {
            liveData.postValue(NetworkResult.Error("Please login again to continue using this feature"))
            return
        }
        if (response.isSuccessful && response.body() != null) {
            liveData.postValue(NetworkResult.Success(response.body()!!))
            return
        }
        val errorMessage = response.errorBody()?.parseErrorMessage() ?: "Something went wrong"
        liveData.postValue(NetworkResult.Error(errorMessage))
    }

    private fun ResponseBody.parseErrorMessage(): String {
        return try {
            JSONObject(string()).getString("message")
        } catch (e: JSONException) {
            Logger.error("JSONException: ${e.message.toString()}")
            "Something went wrong"
        }
    }
}