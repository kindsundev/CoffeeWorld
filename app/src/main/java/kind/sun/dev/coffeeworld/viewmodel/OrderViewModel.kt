package kind.sun.dev.coffeeworld.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.data.model.response.cafe.CafeCategoriesResponse
import kind.sun.dev.coffeeworld.data.model.response.cafe.CafeListResponse
import kind.sun.dev.coffeeworld.data.repository.CafeRepository
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val cafeRepository: CafeRepository
): BaseViewModel(){

    val cafe: LiveData<NetworkResult<CafeListResponse>>
        get() = cafeRepository.cafe

    val categories: LiveData<NetworkResult<CafeCategoriesResponse>>
        get() = cafeRepository.categories

    fun getCafeList() {
        checkThenExecute(null) {
            viewModelScope.launch { cafeRepository.getCafeList() }
        }
    }

    fun getCategoryList(cafeId: Int) {
        checkThenExecute(null) {
            viewModelScope.launch { cafeRepository.getCategoryList(cafeId) }
        }
    }
}