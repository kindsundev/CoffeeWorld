package kind.sun.dev.coffeeworld.data.remote.api

import kind.sun.dev.coffeeworld.data.remote.response.CafeCategoryResponse
import kind.sun.dev.coffeeworld.data.remote.response.CafeDrinksResponse
import kind.sun.dev.coffeeworld.data.remote.response.CafeMenuResponse
import kind.sun.dev.coffeeworld.data.remote.response.CafeResponse
import kind.sun.dev.coffeeworld.data.remote.response.MessageResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface CafeApi {
    @GET("/cafes")
    suspend fun fetchCafes(): Response<CafeResponse>

    @GET("/cafes/{cafe_id}/categories")
    suspend fun fetchCategories(
        @Path("cafe_id") cafeId: Int
    ): Response<CafeCategoryResponse>

    @GET("/cafes/{cafe_id}/drinks")
    suspend fun fetchDrinks(
        @Path("cafe_id") cafeId: Int
    ): Response<CafeDrinksResponse>

    @GET("/cafes/{cafe_id}/category/{category_id}/drinks")
    suspend fun fetchDrinksByCategory(
        @Path("cafe_id") cafeId: Int,
        @Path("category_id") categoryId: Int
    ): Response<CafeDrinksResponse>

    @GET("/cafes/{cafe_id}/menu")
    suspend fun fetchMenu(
        @Path("cafe_id") cafeId: Int
    ): Response<CafeMenuResponse>

    @PUT("/drink/{id}/quantity")
    suspend fun updateQuantityDrinks(
        @Path("id") id: Int,
        @Body quantity: RequestBody
    ): Response<MessageResponse>
}