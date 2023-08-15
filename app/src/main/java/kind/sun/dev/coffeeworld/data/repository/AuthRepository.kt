package kind.sun.dev.coffeeworld.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kind.sun.dev.coffeeworld.data.api.AuthService
import kind.sun.dev.coffeeworld.data.model.request.auth.AuthRequest
import kind.sun.dev.coffeeworld.data.model.request.auth.LoginRequest
import kind.sun.dev.coffeeworld.data.model.request.auth.RegisterRequest
import kind.sun.dev.coffeeworld.data.model.response.auth.LoginResponse
import kind.sun.dev.coffeeworld.data.model.response.common.MessageResponse
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
    private val _authLogin = MutableLiveData<NetworkResult<LoginResponse>>()
    val authLogin: LiveData<NetworkResult<LoginResponse>>
        get() = _authLogin

    private val _authRegister = MutableLiveData<NetworkResult<MessageResponse>>()
    val authRegister: LiveData<NetworkResult<MessageResponse>>
        get() = _authRegister

    private val _authPasswordReset = MutableLiveData<NetworkResult<MessageResponse>>()
    val authPasswordReset: LiveData<NetworkResult<MessageResponse>>
        get() = _authPasswordReset

    private val authExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Logger.error("AuthException: ${throwable.message}")
    }

    suspend fun login(loginRequest: LoginRequest) {
        performAuthOperation(_authLogin) {
            authService.login(loginRequest)
        }
    }

    suspend fun register(registerRequest: RegisterRequest) {
        performAuthOperation(_authRegister) {
            authService.register(registerRequest)
        }
    }

    suspend fun passwordReset(authRequest: AuthRequest) {
        performAuthOperation(_authPasswordReset) {
            authService.passwordReset(authRequest)
        }
    }

    private suspend fun <T> performAuthOperation(
        liveData: MutableLiveData<NetworkResult<T>>,
        operation: suspend () -> Response<T>
    ) {
        liveData.postValue(NetworkResult.Loading())
        withContext(Dispatchers.IO + authExceptionHandler) {
            val response = operation()
            withContext(Dispatchers.Main) {
                handleResponse(response, liveData)
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