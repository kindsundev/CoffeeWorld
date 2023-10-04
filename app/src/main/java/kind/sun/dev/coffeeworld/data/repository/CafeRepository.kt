package kind.sun.dev.coffeeworld.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kind.sun.dev.coffeeworld.base.BaseRepository
import kind.sun.dev.coffeeworld.api.CafeService
import kind.sun.dev.coffeeworld.data.model.response.cafe.CafeCategoriesResponse
import kind.sun.dev.coffeeworld.data.model.response.cafe.CafeListResponse
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import javax.inject.Inject

class CafeRepository @Inject constructor(
    private val cafeService: CafeService
): BaseRepository() {
    private val _cafe = MutableLiveData<NetworkResult<CafeListResponse>>()
    val cafe : LiveData<NetworkResult<CafeListResponse>>
        get() = _cafe

    private val _categories = MutableLiveData<NetworkResult<CafeCategoriesResponse>>()
    val categories: LiveData<NetworkResult<CafeCategoriesResponse>>
        get() = _categories

    suspend fun getCafeList() {
        performNetworkOperation(_cafe) {
            cafeService.getCafeList()
        }
    }

    suspend fun getCategoryList(cafeId: Int) {
        performNetworkOperation(_categories) {
            cafeService.getCategoryList(cafeId)
        }
    }


}