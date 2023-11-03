package kind.sun.dev.coffeeworld.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.SignatureException
import kind.sun.dev.coffeeworld.BuildConfig
import kind.sun.dev.coffeeworld.api.UserAPI
import kind.sun.dev.coffeeworld.base.BaseRepository
import kind.sun.dev.coffeeworld.contract.UserContract
import kind.sun.dev.coffeeworld.data.remote.request.UserEmailRequest
import kind.sun.dev.coffeeworld.data.remote.request.UserPasswordRequest
import kind.sun.dev.coffeeworld.data.remote.response.MessageResponse
import kind.sun.dev.coffeeworld.data.remote.response.UserResponse
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.common.Logger
import kind.sun.dev.coffeeworld.utils.helper.storage.PreferencesHelper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userAPI: UserAPI,
    private val preferences: PreferencesHelper
): BaseRepository(), UserContract.Service {

    private val _user = MutableLiveData<NetworkResult<UserResponse>>()
    override val userResponse: LiveData<NetworkResult<UserResponse>> get() = _user
    override val messageResponse: LiveData<NetworkResult<MessageResponse>> get() = statusMessage

    override val username: String? by lazy {
        preferences.userToken?.let { jwtToken ->
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

    override suspend fun handleFetchUser() {
        username?.let {
            performNetworkOperation(_user) { userAPI.getUser(it) }
        }
    }

    override suspend fun handleUpdateAvatar(avatar: File) {
        username?.let {
            performNetworkOperation(statusMessage) {
                val usernameRequestBody = convertToTextRequestBody(it)
                val avatarRequestBody = avatar.asRequestBody("image/*".toMediaTypeOrNull())
                val avatarPart =
                    MultipartBody.Part.createFormData("image", avatar.name, avatarRequestBody)
                userAPI.updateAvatar(usernameRequestBody, avatarPart)
            }
        }
    }

    override suspend fun handleUpdateEmail(email: String, password: String) {
        username?.let {
            performNetworkOperation(statusMessage) {
                userAPI.updateEmail(UserEmailRequest(it, email, password))
            }
        }
    }

    override suspend fun handleUpdateName(name: String) {
        username?.let {
            performNetworkOperation(statusMessage) {
                userAPI.updateName(it, convertToTextRequestBody(name))
            }
        }
    }

    override suspend fun handleUpdateAddress(address: String) {
        username?.let {
            performNetworkOperation(statusMessage) {
                userAPI.updateAddress(it, convertToTextRequestBody(address))
            }
        }
    }

    override suspend fun handleUpdatePhone(phone: String) {
        username?.let {
            performNetworkOperation(statusMessage) {
                userAPI.updatePhone(it, convertToTextRequestBody(phone))
            }
        }
    }


    override suspend fun handleUpdatePassword(currentPassword: String, newPassword: String) {
        username?.let {
            performNetworkOperation(statusMessage) {
                userAPI.updatePassword(UserPasswordRequest(it, currentPassword, newPassword))
            }
        }
    }


}