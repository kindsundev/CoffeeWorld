package kind.sun.dev.coffeeworld.data.api

import kind.sun.dev.coffeeworld.data.model.response.cafe.CafeListResponse
import retrofit2.Response
import retrofit2.http.GET

interface CafeService {

    @GET("/cafes")
    suspend fun getCafeList(): Response<CafeListResponse>

}