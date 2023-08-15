package kind.sun.dev.coffeeworld.ui.cafe

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.data.model.response.cafe.CafeModel
import kind.sun.dev.coffeeworld.data.model.response.cafe.CafeListResponse
import kind.sun.dev.coffeeworld.data.repository.CafeRepository
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.network.NetworkHelper
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CafeViewModel @Inject constructor(
    private val cafeRepository: CafeRepository,
    private val networkHelper: NetworkHelper
): ViewModel() {
    val cafe: LiveData<NetworkResult<CafeListResponse>>
        get() = cafeRepository.cafe

    fun getListCafe() {
        viewModelScope.launch {
            if (networkHelper.isConnected) {
                cafeRepository.getListCafe()
            }
        }
    }

    fun filterListByName(name: String, list: List<CafeModel>): List<CafeModel> {
        val formatName = name.lowercase()
        return list.filter { item ->
            item.name.lowercase().contains(formatName)
        }
    }

}