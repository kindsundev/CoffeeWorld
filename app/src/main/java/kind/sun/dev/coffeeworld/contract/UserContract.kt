package kind.sun.dev.coffeeworld.contract

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kind.sun.dev.coffeeworld.data.local.model.UserModel
import kind.sun.dev.coffeeworld.data.remote.request.UserEmailRequest
import kind.sun.dev.coffeeworld.data.remote.request.UserPasswordRequest
import kind.sun.dev.coffeeworld.data.remote.response.MessageResponse
import kind.sun.dev.coffeeworld.data.remote.response.UserResponse
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import java.io.File

interface UserContract {

    interface Validator {

        fun validateUpdateName(name: String): Pair<Boolean, String>

        fun validateUpdateAddress(address: String): Pair<Boolean, String>

        fun validatorUpdateEmail(email: String, password: String): Pair<Boolean, String>

        fun validateUpdatePhone(phoneNumber: String): Pair<Boolean, String>

        fun validateUpdatePassword(
            currentPassword: String,
            newPassword: String,
            reNewPassword: String
        ): Pair<Boolean, String>
    }


    interface ViewModel {
        fun onFetchUser(
            onDataFromLocal: (UserModel?) -> Unit,
            onFailedMessage: (String) -> Unit
        )

        suspend fun onSyncUser(userModel: UserModel)

        fun onUpdateAvatar(avatar: File, onFailedMessage: (String) -> Unit)

        fun onUpdateName(onFailedMessage: (String) -> Unit)

        fun onUpdateEmail(onFailedMessage: (String) -> Unit)

        fun onUpdatePassword(onFailedMessage: (String) -> Unit)

        fun onUpdateAddress(onFailedMessage: (String) -> Unit)

        fun onUpdatePhone(onFailedMessage: (String) -> Unit)
    }


    interface Service {
        val userResponse: LiveData<NetworkResult<UserResponse>>
        val messageResponse: LiveData<NetworkResult<MessageResponse>>
        val username: String?

        suspend fun handleFetchUser()

        suspend fun handleSyncUser(userModel: UserModel)

        suspend fun handleGetUser(): UserModel?

        suspend fun handleUpdateAvatar(avatar: File)

        suspend fun handleUpdateEmail(email: String, password: String)

        suspend fun handleUpdateName(name: String)

        suspend fun handleUpdateAddress(address: String)

        suspend fun handleUpdatePhone(phone: String)

        suspend fun handleUpdatePassword(currentPassword: String, newPassword: String)
    }


    interface API {
        @GET("/user/{username}")
        suspend fun getUser(@Path("username") username: String): Response<UserResponse>

        @PUT("/user/email")
        suspend fun updateEmail(@Body userEmailRequest: UserEmailRequest): Response<MessageResponse>

        @PUT("/user/password")
        suspend fun updatePassword(@Body userPasswordRequest: UserPasswordRequest): Response<MessageResponse>

        @PUT("/user/name/{username}")
        suspend fun updateName(
            @Path("username") username: String,
            @Body name: RequestBody
        ): Response<MessageResponse>

        @PUT("/user/phone/{username}")
        suspend fun updatePhone(
            @Path("username") username: String,
            @Body phone: RequestBody
        ): Response<MessageResponse>

        @PUT("/user/address/{username}")
        suspend fun updateAddress(
            @Path("username") username: String,
            @Body address: RequestBody
        ): Response<MessageResponse>

        @Multipart
        @PUT("/user/avatar")
        suspend fun updateAvatar(
            @Part("username") username: RequestBody,
            @Part image: MultipartBody.Part
        ): Response<MessageResponse>
    }


    @Dao
    interface DAO {
        @Query("SELECT * FROM user_table LIMIT 1")
        fun getUser(): UserModel?

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun upsertUser(user: UserModel)

        @Delete
        suspend fun deleteUser(user: UserModel)
    }
}