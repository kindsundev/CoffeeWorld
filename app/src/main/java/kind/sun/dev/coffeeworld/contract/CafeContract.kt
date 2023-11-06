package kind.sun.dev.coffeeworld.contract

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.data.local.model.MenuModel
import kind.sun.dev.coffeeworld.data.remote.response.CafeCategoryResponse
import kind.sun.dev.coffeeworld.data.remote.response.CafeDrinksResponse
import kind.sun.dev.coffeeworld.data.remote.response.CafeMenuResponse
import kind.sun.dev.coffeeworld.data.remote.response.CafeResponse
import kind.sun.dev.coffeeworld.data.remote.response.MessageResponse
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.view.adapter.cafe.CafeShopViewItem
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface CafeContract {

    interface ViewModel {
        suspend fun onFetchAllCafes(onDataFromLocal: (List<CafeModel>?) -> Unit, onFailedMessage: (String) -> Unit)

        suspend fun onSyncAllCafes(cafes: List<CafeModel>)

        suspend fun onFetchMenuForCafe(cafeId: Int)

        suspend fun onSyncMenuOfCafe(menus: List<MenuModel>)

        fun convertToCafeViewItem(title: Array<String>, data: List<CafeModel>): MutableList<CafeShopViewItem>

        fun filterCafeList(name: String, list: List<CafeShopViewItem>): List<CafeShopViewItem>
    }


    interface Service {
        val cafeResponse: LiveData<NetworkResult<CafeResponse>>
        val menuResponse: LiveData<NetworkResult<CafeMenuResponse>>

        suspend fun handleFetchAllCafes()

        suspend fun handleSyncAllCafes(cafes: List<CafeModel>)

        suspend fun handleGetAllCafes(): List<CafeModel>?

        suspend fun handleFetchMenu(cafeId: Int)

        suspend fun handleSyncMenu(menus: List<MenuModel>)
    }


    interface API {
        @GET("/cafes")
        suspend fun fetchCafes(): Response<CafeResponse>

        @GET("/cafes/{cafe_id}/categories")
        suspend fun fetchCategories(@Path("cafe_id") cafeId: Int): Response<CafeCategoryResponse>

        @GET("/cafes/{cafe_id}/drinks")
        suspend fun fetchDrinks(@Path("cafe_id") cafeId: Int): Response<CafeDrinksResponse>

        @GET("/cafes/{cafe_id}/category/{category_id}/drinks")
        suspend fun fetchDrinksByCategory(
            @Path("cafe_id") cafeId: Int,
            @Path("category_id") categoryId: Int
        ): Response<CafeDrinksResponse>

        @GET("/cafes/{cafe_id}/menu")
        suspend fun fetchMenus(@Path("cafe_id") cafeId: Int): Response<CafeMenuResponse>

        @PUT("/drinks/{id}/quantity")
        suspend fun updateQuantityDrinks(
            @Path("id") id: Int,
            @Body quantity: RequestBody
        ): Response<MessageResponse>
    }

    @Dao
    interface DAO {
        @Query("SELECT * FROM cafe_table")
        suspend fun getAllCafes(): List<CafeModel>?

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun upsertCafe(cafe: CafeModel)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun upsertAllCafes(cafes: List<CafeModel>)

        @Delete
        suspend fun deleteCafe(cafe: CafeModel)

        @Query("DELETE FROM cafe_table")
        suspend fun deleteAllCafes()
    }

}