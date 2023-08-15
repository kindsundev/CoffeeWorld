package kind.sun.dev.coffeeworld.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.SignatureException
import kind.sun.dev.coffeeworld.BuildConfig
import kind.sun.dev.coffeeworld.base.BaseRepository
import kind.sun.dev.coffeeworld.data.api.UserService
import kind.sun.dev.coffeeworld.data.model.request.user.UserEmailRequest
import kind.sun.dev.coffeeworld.data.model.request.user.UserPasswordRequest
import kind.sun.dev.coffeeworld.data.model.response.common.MessageResponse
import kind.sun.dev.coffeeworld.data.model.response.user.UserResponse
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.api.TokenManager
import kind.sun.dev.coffeeworld.utils.common.Logger
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService,
    private val tokenManager: TokenManager
): BaseRepository() {
    private val username: String?

    init { username = getUserNameFromJWTToken() }

    private val _user = MutableLiveData<NetworkResult<UserResponse>>()
    val user: LiveData<NetworkResult<UserResponse>>
        get() = _user

    private val _userUpdate = MutableLiveData<NetworkResult<MessageResponse>>()
    val userUpdate: LiveData<NetworkResult<MessageResponse>>
        get() = _userUpdate


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
            performNetworkOperation(_user) { userService.getUser(it) }
        }
    }

    suspend fun updateAvatar(avatar: File) {
        username?.let {
            performNetworkOperation(_userUpdate) {
                val usernameRequestBody = convertToTextRequestBody(it)
                val avatarRequestBody = avatar.asRequestBody("image/*".toMediaTypeOrNull())
                val avatarPart =
                    MultipartBody.Part.createFormData("image", avatar.name, avatarRequestBody)
                userService.updateAvatar(usernameRequestBody, avatarPart)
            }
        }
    }

    suspend fun updateEmail(email: String, password: String) {
        username?.let {
            performNetworkOperation(_userUpdate) {
                userService.updateEmail(UserEmailRequest(it, email, password))
            }
        }
    }

    suspend fun updatePassword(currentPassword: String, newPassword: String) {
        username?.let {
            performNetworkOperation(_userUpdate) {
                userService.updatePassword(UserPasswordRequest(it, currentPassword, newPassword))
            }
        }
    }

    suspend fun updateName(name: String) {
        username?.let {
            performNetworkOperation(_userUpdate) {
                userService.updateName(it, convertToTextRequestBody(name))
            }
        }
    }

    suspend fun updateAddress(address: String) {
        username?.let {
            performNetworkOperation(_userUpdate) {
                userService.updateAddress(it, convertToTextRequestBody(address))
            }
        }
    }

    suspend fun updatePhone(phone: String) {
        username?.let {
            performNetworkOperation(_userUpdate) {
                userService.updatePhone(it, convertToTextRequestBody(phone))
            }
        }
    }

}