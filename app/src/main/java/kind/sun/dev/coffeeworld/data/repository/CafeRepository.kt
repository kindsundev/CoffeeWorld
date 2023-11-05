package kind.sun.dev.coffeeworld.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kind.sun.dev.coffeeworld.api.CafeAPI
import kind.sun.dev.coffeeworld.base.BaseRepository
import kind.sun.dev.coffeeworld.contract.CafeContract
import kind.sun.dev.coffeeworld.data.remote.response.CafeMenuResponse
import kind.sun.dev.coffeeworld.data.remote.response.CafeResponse
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import javax.inject.Inject

class CafeRepository @Inject constructor(
    private val cafeAPI: CafeAPI
): BaseRepository(), CafeContract.Service {
    private val _cafe = MutableLiveData<NetworkResult<CafeResponse>>()
    private val _menu = MutableLiveData<NetworkResult<CafeMenuResponse>>()
    override val cafeResponse: LiveData<NetworkResult<CafeResponse>> get() = _cafe
    override val menuResponse: LiveData<NetworkResult<CafeMenuResponse>> get() = _menu


    override suspend fun handleFetchAllCafes() {
        performNetworkOperation(
            networkRequest = { cafeAPI.fetchCafes() },
            networkResult = _cafe
        )
    }

    override suspend fun handleFetchMenu(cafeId: Int) {

    }

}