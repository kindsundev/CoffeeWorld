package kind.sun.dev.coffeeworld.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kind.sun.dev.coffeeworld.base.BaseRepository
import kind.sun.dev.coffeeworld.contract.CafeContract
import kind.sun.dev.coffeeworld.data.local.dao.CafeDAO
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.data.local.model.MenuModel
import kind.sun.dev.coffeeworld.data.remote.api.CafeAPI
import kind.sun.dev.coffeeworld.data.remote.response.CafeMenuResponse
import kind.sun.dev.coffeeworld.data.remote.response.CafeResponse
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class CafeRepository @Inject constructor(
    private val remoteAPI: CafeAPI,
    private val localDAO: CafeDAO
): BaseRepository(), CafeContract.Service {
    private val _cafe = MutableLiveData<NetworkResult<CafeResponse>>()
    private val _menu = MutableLiveData<NetworkResult<CafeMenuResponse>>()
    override val cafeResponse: LiveData<NetworkResult<CafeResponse>> get() = _cafe
    override val menuResponse: LiveData<NetworkResult<CafeMenuResponse>> get() = _menu


    override suspend fun handleFetchAllCafes() {
        performNetworkOperation(
            networkRequest = { remoteAPI.fetchCafes() },
            networkResult = _cafe
        )
    }

    override suspend fun handleSyncAllCafes(cafes: List<CafeModel>) {
        coroutineScope.launch { localDAO.upsertAllCafes(cafes) }
    }

    override suspend fun handleGetAllCafes(): List<CafeModel>? {
        return coroutineScope.async { localDAO.getAllCafes() }.await()
    }

    override suspend fun handleFetchMenu(cafeId: Int) {

    }

    override suspend fun handleSyncMenu(menus: List<MenuModel>) {

    }

}