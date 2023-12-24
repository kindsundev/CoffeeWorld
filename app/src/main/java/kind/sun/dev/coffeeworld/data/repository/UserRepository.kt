package kind.sun.dev.coffeeworld.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.SignatureException
import kind.sun.dev.coffeeworld.BuildConfig
import kind.sun.dev.coffeeworld.base.BaseRepository
import kind.sun.dev.coffeeworld.contract.UserContract
import kind.sun.dev.coffeeworld.data.local.dao.UserDao
import kind.sun.dev.coffeeworld.data.local.entity.UserEntity
import kind.sun.dev.coffeeworld.data.local.model.UserModel
import kind.sun.dev.coffeeworld.data.remote.api.UserApi
import kind.sun.dev.coffeeworld.data.remote.request.UserEmailRequest
import kind.sun.dev.coffeeworld.data.remote.request.UserPasswordRequest
import kind.sun.dev.coffeeworld.data.remote.response.MessageResponse
import kind.sun.dev.coffeeworld.data.remote.response.UserResponse
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.common.Logger
import kind.sun.dev.coffeeworld.utils.helper.storage.PreferencesHelper
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val remoteApi: UserApi,
    private val localDao: UserDao,
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
                Logger.error(message = "Invalid signature: ${e.message}")
                null
            } catch (e: ExpiredJwtException) {
                Logger.error(message = "Token has expired: ${e.message}")
                null
            } catch (e: Exception) {
                Logger.error(message = "Decrypt token: ${e.message}")
                null
            }
        }
    }

    override suspend fun handleFetchUser(isLoading: Boolean) {
        username?.let {
            performNetworkOperation(
                isShowProgress = !isLoading,
                networkRequest = { remoteApi.getUser(it) },
                networkResult = _user
            )
        }
    }

    override suspend fun handleSyncUser(userModel: UserModel) {
        coroutineScope.launch {
            localDao.upsertUser(
                UserEntity(Constants.DB_USER_KEY, userModel)
            )
        }
    }

    override suspend fun handleGetUser(id: String): UserEntity? {
        return coroutineScope.async { localDao.getUser(id) }.await()
    }

    override suspend fun handleUpdateAvatar(avatar: File) {
        username?.let {
            performNetworkOperation(
                networkRequest = {
                    val usernameRequestBody = convertToTextRequestBody(it)
                    val avatarRequestBody = avatar.asRequestBody("image/*".toMediaTypeOrNull())
                    val avatarPart =
                        MultipartBody.Part.createFormData("image", avatar.name, avatarRequestBody)
                    remoteApi.updateAvatar(usernameRequestBody, avatarPart)
                },
                networkResult = statusMessage
            )
        }
    }

    override suspend fun handleUpdateEmail(email: String, password: String) {
        username?.let {
            performNetworkOperation(
                networkRequest = { remoteApi.updateEmail(UserEmailRequest(it, email, password)) },
                networkResult = statusMessage
            )
        }
    }

    override suspend fun handleUpdateName(name: String) {
        username?.let {
            performNetworkOperation(
                networkRequest = { remoteApi.updateName(it, convertToTextRequestBody(name)) },
                networkResult = statusMessage
            )
        }
    }

    override suspend fun handleUpdateAddress(address: String) {
        username?.let {
            performNetworkOperation(
                networkRequest = { remoteApi.updateAddress(it, convertToTextRequestBody(address)) },
                networkResult = statusMessage
            )
        }
    }

    override suspend fun handleUpdatePhone(phone: String) {
        username?.let {
            performNetworkOperation(
                networkRequest = { remoteApi.updatePhone(it, convertToTextRequestBody(phone)) },
                networkResult = statusMessage
            )
        }
    }

    override suspend fun handleUpdatePassword(currentPassword: String, newPassword: String) {
        username?.let {
            performNetworkOperation(
                networkRequest = { remoteApi.updatePassword(UserPasswordRequest(it, currentPassword, newPassword)) },
                networkResult = statusMessage
            )
        }
    }
}