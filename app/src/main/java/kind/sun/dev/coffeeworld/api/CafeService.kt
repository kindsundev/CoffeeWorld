package kind.sun.dev.coffeeworld.api

import kind.sun.dev.coffeeworld.data.remote.response.CafeCategoryResponse
import kind.sun.dev.coffeeworld.data.remote.response.CafeDrinksResponse
import kind.sun.dev.coffeeworld.data.remote.response.CafeResponse
import kind.sun.dev.coffeeworld.data.remote.response.MessageResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface CafeService {

    @GET("/cafes")
    suspend fun getCafeList(): Response<CafeResponse>

    @GET("/cafes/{cafe_id}/categories")
    suspend fun getCategoryList(@Path("cafe_id") cafeId: Int): Response<CafeCategoryResponse>

    @GET("/cafes/{cafe_id}/category/{category_id}/drinks")
    suspend fun getDrinksListInCategory(
        @Path("cafe_id") cafeId: Int, @Path("category_id") categoryId: Int
    ): Response<CafeDrinksResponse>

    @PUT("/drinks/{id}/quantity")
    suspend fun updateQuantityDrinks(
        @Path("id") id: Int, @Body quantity: RequestBody
    ): Response<MessageResponse>

}