package kind.sun.dev.coffeeworld.ui.cafe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.data.model.response.cafe.ListCafeResponse
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
    var noInternetLiveData = MutableLiveData<Boolean>(false)
    val cafeResponseLiveData: LiveData<NetworkResult<ListCafeResponse>>
        get() = cafeRepository.cafeResponseLiveData

    fun getListCafe() {
        viewModelScope.launch {
            noInternetLiveData.value = false
            if (networkHelper.isConnected) {
                cafeRepository.getListCafe()
            } else {
                noInternetLiveData.value = true
            }
        }
    }
}