package kind.sun.dev.coffeeworld.base

import androidx.lifecycle.MutableLiveData
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.common.Logger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

open class BaseRepository {

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
        try {
            liveData.postValue(NetworkResult.Loading())
            withContext(Dispatchers.IO + baseExceptionHandler) {
                val response = operation()
                withContext(Dispatchers.Main) {
                    handleNetworkResponse(response, liveData)
                }
            }
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    protected fun <T> handleNetworkResponse(
        response: Response<T>?,
        liveData: MutableLiveData<NetworkResult<T>>
    ) {
        when {
            response == null -> {
                liveData.postValue(NetworkResult.Error(Constants.REQUEST_LOGIN))
            }
            response.isSuccessful && response.body() != null -> {
                liveData.postValue(NetworkResult.Success(response.body()!!))
            }
            response.errorBody() != null -> {
                try {
                    val errorJSON = JSONObject(response.errorBody()!!.string())
                    val errorMessage = errorJSON.getString("message")
                    liveData.postValue(NetworkResult.Error(errorMessage))
                } catch (e: JSONException) {
                    Logger.error("JSONException: ${e.message.toString()}")
                }
            }
            else -> {
                liveData.postValue(NetworkResult.Error("Something went wrong"))
            }
        }
    }
}