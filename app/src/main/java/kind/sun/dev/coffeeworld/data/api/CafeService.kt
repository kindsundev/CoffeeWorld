package kind.sun.dev.coffeeworld.data.api

import kind.sun.dev.coffeeworld.data.model.response.cafe.CafeCategoriesResponse
import kind.sun.dev.coffeeworld.data.model.response.cafe.CafeDrinksResponse
import kind.sun.dev.coffeeworld.data.model.response.cafe.CafeListResponse
import kind.sun.dev.coffeeworld.data.model.response.common.MessageResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface CafeService {

    @GET("/cafes")
    suspend fun getCafeList(): Response<CafeListResponse>

    @GET("/cafes/{cafe_id}/categories")
    suspend fun getCategoryList(@Path("cafe_id") cafeId: Int): Response<CafeCategoriesResponse>

    @GET("/cafes/{cafe_id}/category/{category_id}/drinks")
    suspend fun getDrinksListInCategory(
        @Path("cafe_id") cafeId: Int, @Path("category_id") categoryId: Int
    ): Response<CafeDrinksResponse>

    @PUT("/drinks/{id}/quantity")
    suspend fun updateQuantityDrinks(
        @Path("id") id: Int, @Body quantity: RequestBody
    ): Response<MessageResponse>

}