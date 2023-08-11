package kind.sun.dev.coffeeworld.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.SignatureException
import kind.sun.dev.coffeeworld.BuildConfig
import kind.sun.dev.coffeeworld.data.api.UserService
import kind.sun.dev.coffeeworld.data.model.request.user.UserEmailRequest
import kind.sun.dev.coffeeworld.data.model.request.user.UserPasswordRequest
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
        username?.let {
            performUserAction(_userResponseLiveData) { userService.getUser(it) }
        }
    }

    suspend fun updateAvatar(avatar: File) {
        username?.let {
            performUserAction(_userUpdateResponseLiveData) {
                val usernameRequestBody = it.toRequestBody("text/plain".toMediaTypeOrNull())
                val avatarRequestBody = avatar.asRequestBody("image/*".toMediaTypeOrNull())
                val avatarPart = MultipartBody.Part.createFormData("image", avatar.name, avatarRequestBody)
                userService.updateAvatar(usernameRequestBody, avatarPart)
            }
        }
    }

    suspend fun updateEmail(email: String, password: String) {
        username?.let {
            performUserAction(_userUpdateResponseLiveData) {
                userService.updateEmail(UserEmailRequest(it, email, password))
            }
        }
    }

    suspend fun updatePassword(currentPassword: String, newPassword: String) {
        username?.let {
            performUserAction(_userUpdateResponseLiveData) {
                userService.updatePassword(UserPasswordRequest(it, currentPassword, newPassword))
            }
        }
    }

    private suspend fun <T> performUserAction(
        liveData: MutableLiveData<NetworkResult<T>>,
        action: suspend () -> Response<T>
    ) {
        liveData.postValue(NetworkResult.Loading())
        withContext(Dispatchers.IO + userExceptionHandler) {
            val response = action()
            withContext(Dispatchers.Main) {
                handleResponse(response, liveData)
            }
        }
    }

    suspend fun updateName(name: String) {
        updateWithTextPlain(name) { username, requestBody ->
            userService.updateName(username, requestBody)
        }
    }

    suspend fun updateAddress(address: String) {
        updateWithTextPlain(address) { username, requestBody ->
            userService.updateAddress(username, requestBody)
        }
    }

    suspend fun updatePhone(phone: String) {
        updateWithTextPlain(phone) { username, requestBody ->
            userService.updatePhone(username, requestBody)
        }
    }

    private suspend fun updateWithTextPlain(
        content: String,
        updateData: suspend (username: String, requestBody: RequestBody) -> Response<UserUpdateResponse>
    ) {
        _userUpdateResponseLiveData.postValue(NetworkResult.Loading())
        val request = content.toRequestBody("text/plain".toMediaType())
        withContext(Dispatchers.IO + userExceptionHandler) {
            username?.let {
                val response = updateData(it, request)
                withContext(Dispatchers.Main) {
                    handleResponse(response, _userUpdateResponseLiveData)
                }
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