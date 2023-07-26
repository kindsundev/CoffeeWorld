package kind.sun.dev.coffeeworld.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kind.sun.dev.coffeeworld.data.api.CafeService
import kind.sun.dev.coffeeworld.data.model.response.cafe.ListCafeResponse
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.common.Logger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class CafeRepository @Inject constructor(
    private val cafeService: CafeService
) {
    private val _cafeResponseLiveData = MutableLiveData<NetworkResult<ListCafeResponse>>()
    val cafeResponseLiveData : LiveData<NetworkResult<ListCafeResponse>>
        get() = _cafeResponseLiveData

    private val cafeExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Logger.error("CafeException: ${throwable.message}")
    }

    suspend fun getListCafe() {
        _cafeResponseLiveData.let {
            it.postValue(NetworkResult.Loading())
            withContext(Dispatchers.IO + cafeExceptionHandler) {
                val response = cafeService.getListCafe()
                withContext(Dispatchers.Main) {
                    handleResponse(response, it)
                }
            }
        }
    }

    private fun handleResponse(response: Response<ListCafeResponse>, liveData: MutableLiveData<NetworkResult<ListCafeResponse>>) {
        if (response.isSuccessful && response.body() != null) {
            liveData.postValue(NetworkResult.Success(response.body()))
        } else if (response.errorBody() != null) {
            try {
                val errorJSON = JSONObject(response.errorBody().toString())
                val errorMessage = errorJSON.getString("message")
                liveData.postValue(NetworkResult.Error(errorMessage))
            } catch (e: JSONException) {
                Logger.error("JSONException: ${e.message.toString()}")
            }
        } else {
            liveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

}