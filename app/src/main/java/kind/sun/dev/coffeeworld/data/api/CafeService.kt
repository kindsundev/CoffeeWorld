package kind.sun.dev.coffeeworld.data.api

import kind.sun.dev.coffeeworld.data.model.response.cafe.CafeResponse
import kind.sun.dev.coffeeworld.data.model.response.cafe.ListCafeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CafeService {

    @GET("/cafes")
    suspend fun getListCafe(): Response<ListCafeResponse>

    @GET("/cafes/{id}")
    suspend fun getCafe(@Path("id") id: Int): Response<CafeResponse>

}