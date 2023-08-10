package kind.sun.dev.coffeeworld.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.SignatureException
import kind.sun.dev.coffeeworld.BuildConfig
import kind.sun.dev.coffeeworld.data.api.UserService
import kind.sun.dev.coffeeworld.data.model.response.user.UserResponse
import kind.sun.dev.coffeeworld.data.model.response.user.UserUpdateResponse
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.api.TokenManager
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.common.Logger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService,
    private val tokenManager: TokenManager
) {
    private val username: String?

    init {
        username = getUserNameFromJWTToken()
    }

    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData

    private val _userUpdateResponseLiveData = MutableLiveData<NetworkResult<UserUpdateResponse>>()
    val userUpdateResponseLiveData: LiveData<NetworkResult<UserUpdateResponse>>
        get() = _userUpdateResponseLiveData

    private val userExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Logger.error("UserException: ${throwable.message}")
    }

    private fun getUserNameFromJWTToken(): String? {
        val token = tokenManager.getToken()
        return token?.let { jwtToken ->
            try {
                val claims: Claims = Jwts.parserBuilder()
                    .setSigningKey(BuildConfig.TOKEN_SECRET.toByteArray())
                    .build()
                    .parseClaimsJws(jwtToken)
                    .body
                claims["username"] as String?
            } catch (e: SignatureException) {
                Logger.error("Invalid signature: ${e.message}")
                null
            } catch (e: ExpiredJwtException) {
                Logger.error("Token has expired: ${e.message}")
                null
            } catch (e: Exception) {
                Logger.error("Decrypt token: ${e.message}")
                null
            }
        }
    }

    suspend fun getUser() {
        _userResponseLiveData.let { liveData ->
            liveData.postValue(NetworkResult.Loading())
            withContext(Dispatchers.IO + userExceptionHandler) {
                val response = username?.let { userService.getUser(it) }
                withContext(Dispatchers.Main) {
                    handleResponse(response, liveData)
                }
            }
        }
    }

    suspend fun updateAvatar(avatar: File) {
        _userUpdateResponseLiveData.let { liveData ->
            liveData.postValue(NetworkResult.Loading())
            withContext(Dispatchers.IO + userExceptionHandler) {
                username?.let {
                    val usernameRequestBody = it.toRequestBody("text/plain".toMediaTypeOrNull())
                    val avatarRequestBody = avatar.asRequestBody("image/*".toMediaTypeOrNull())
                    val avatarPart = MultipartBody.Part.createFormData("image", avatar.name, avatarRequestBody)
                    val response = userService.updateAvatar(usernameRequestBody, avatarPart)
                    withContext(Dispatchers.Main) {
                        handleResponse(response, liveData)
                    }
                }
            }
        }
    }

    suspend fun updateName(name: String) {
        updateByTextPlain(name) { username, requestBody ->
            userService.updateName(username, requestBody)
        }?.let {
            withContext(Dispatchers.Main) {
                handleResponse(it, _userUpdateResponseLiveData)
            }
        }
    }

    suspend fun updateAddress(address: String) {
        updateByTextPlain(address) { username, requestBody ->
            userService.updateAddress(username, requestBody)
        }?.let {
            withContext(Dispatchers.Main) {
                handleResponse(it, _userUpdateResponseLiveData)
            }
        }
    }

    suspend fun updatePhone(phone: String) {
        updateByTextPlain(phone) { username, requestBody ->
            userService.updatePhone(username, requestBody)
        }?.let {
            withContext(Dispatchers.Main) {
                handleResponse(it, _userUpdateResponseLiveData)
            }
        }
    }

    private suspend fun <T> updateByTextPlain(
        content: String, updateData: suspend (username: String, requestBody: RequestBody) -> Response<T>
    ): Response<T>? {
        _userUpdateResponseLiveData.postValue(NetworkResult.Loading())
        val request = content.toRequestBody("text/plain".toMediaType())
        return withContext(Dispatchers.IO + userExceptionHandler) {
            username?.let {
                updateData(it, request)
            }
        }
    }

    private fun <T> handleResponse(response: Response<T>?, liveData: MutableLiveData<NetworkResult<T>>) {
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