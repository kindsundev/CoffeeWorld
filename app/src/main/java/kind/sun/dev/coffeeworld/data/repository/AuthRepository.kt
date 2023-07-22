package kind.sun.dev.coffeeworld.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kind.sun.dev.coffeeworld.data.api.AuthService
import kind.sun.dev.coffeeworld.data.model.request.auth.LoginRequest
import kind.sun.dev.coffeeworld.data.model.request.auth.RegisterRequest
import kind.sun.dev.coffeeworld.data.model.response.auth.LoginResponse
import kind.sun.dev.coffeeworld.data.model.response.auth.RegisterResponse
import kind.sun.dev.coffeeworld.utils.common.Logger
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService
) {
    private val _authLoginResponseLiveData = MutableLiveData<NetworkResult<LoginResponse>>()
    val authLoginResponseLiveData: LiveData<NetworkResult<LoginResponse>>
        get() = _authLoginResponseLiveData

    private val _authRegisterResponseLiveData = MutableLiveData<NetworkResult<RegisterResponse>>()
    val authRegisterResponseLiveData: LiveData<NetworkResult<RegisterResponse>>
        get() = _authRegisterResponseLiveData

    private val authExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Logger.error("AuthException: ${throwable.message}")
    }

    suspend fun login(loginRequest: LoginRequest) {
        _authLoginResponseLiveData.let {
            it.postValue(NetworkResult.Loading())
            withContext(Dispatchers.IO + authExceptionHandler) {
                val response = authService.login(loginRequest)
                withContext(Dispatchers.Main) {
                    handleResponse(response, it)
                }
            }
        }
    }

    suspend fun register(registerRequest: RegisterRequest) {
        _authRegisterResponseLiveData.let {
            it.postValue(NetworkResult.Loading())
            withContext(Dispatchers.IO + authExceptionHandler) {
                val response = authService.register(registerRequest)
                withContext(Dispatchers.Main) {
                    handleResponse(response, it)
                }
            }
        }
    }

    private fun <T> handleResponse(response: Response<T>, liveData: MutableLiveData<NetworkResult<T>>) {
        if (response.isSuccessful && response.body() != null) {
            liveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            try {
                val errorJSON = JSONObject(response.errorBody()!!.string())
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