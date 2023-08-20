package kind.sun.dev.coffeeworld.ui.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.data.model.response.cafe.CafeCategoriesResponse
import kind.sun.dev.coffeeworld.data.model.response.cafe.CafeListResponse
import kind.sun.dev.coffeeworld.data.repository.CafeRepository
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.network.NetworkHelper
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val cafeRepository: CafeRepository,
    private val networkHelper: NetworkHelper
): ViewModel() {

    val cafe: LiveData<NetworkResult<CafeListResponse>>
        get() = cafeRepository.cafe

    val categories: LiveData<NetworkResult<CafeCategoriesResponse>>
        get() = cafeRepository.categories

    fun getListCafe() {
        viewModelScope.launch {
            if (networkHelper.isConnected) {
                cafeRepository.getCafeList()
            }
        }
    }

    fun getCategoryList(cafeId: Int) {
        viewModelScope.launch {
            if (networkHelper.isConnected) {
                cafeRepository.getCategoryList(cafeId)
            }
        }
    }
}