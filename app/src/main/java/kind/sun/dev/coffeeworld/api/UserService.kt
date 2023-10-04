package kind.sun.dev.coffeeworld.api

import kind.sun.dev.coffeeworld.data.model.request.user.UserEmailRequest
import kind.sun.dev.coffeeworld.data.model.request.user.UserPasswordRequest
import kind.sun.dev.coffeeworld.data.model.response.common.MessageResponse
import kind.sun.dev.coffeeworld.data.model.response.user.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface UserService {

    @GET("/user/{username}")
    suspend fun getUser(
        @Path("username") username: String
    ): Response<UserResponse>

    @PUT("/user/email")
    suspend fun updateEmail(
        @Body userEmailRequest: UserEmailRequest
    ): Response<MessageResponse>

    @PUT("/user/password")
    suspend fun updatePassword(
        @Body userPasswordRequest: UserPasswordRequest
    ): Response<MessageResponse>

    @PUT("/user/name/{username}")
    suspend fun updateName(
        @Path("username") username: String, @Body name: RequestBody
    ): Response<MessageResponse>

    @PUT("/user/phone/{username}")
    suspend fun updatePhone(
        @Path("username") username: String, @Body phone: RequestBody
    ): Response<MessageResponse>

    @PUT("/user/address/{username}")
    suspend fun updateAddress(
        @Path("username") username: String, @Body address: RequestBody
    ): Response<MessageResponse>

    @Multipart
    @PUT("/user/avatar")
    suspend fun updateAvatar(
        @Part("username") username: RequestBody, @Part image: MultipartBody.Part
    ): Response<MessageResponse>

}