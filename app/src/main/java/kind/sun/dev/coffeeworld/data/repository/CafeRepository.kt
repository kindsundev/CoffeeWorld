package kind.sun.dev.coffeeworld.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kind.sun.dev.coffeeworld.base.BaseRepository
import kind.sun.dev.coffeeworld.contract.CafeContract
import kind.sun.dev.coffeeworld.data.local.dao.CafeDao
import kind.sun.dev.coffeeworld.data.local.entity.CafeEntity
import kind.sun.dev.coffeeworld.data.local.entity.MenuEntity
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.data.local.model.MenuModel
import kind.sun.dev.coffeeworld.data.remote.api.CafeApi
import kind.sun.dev.coffeeworld.data.remote.response.CafeMenuResponse
import kind.sun.dev.coffeeworld.data.remote.response.CafeResponse
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.common.Constants
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
* Why test load variables like this?
* Because it involves handling the display of the CustomLoadingDialog or LoadingDialog of the SwipeLayout
* Detail: if view set false: CustomLoadingDialog will shows, otherwise LoadingDialog of SwipeLayout will shows
* */

class CafeRepository @Inject constructor(
    private val remoteApi: CafeApi,
    private val localDao: CafeDao
): BaseRepository(), CafeContract.Service {
    private val _cafe = MutableLiveData<NetworkResult<CafeResponse>>()
    private val _menu = MutableLiveData<NetworkResult<CafeMenuResponse>>()
    override val cafeResponse: LiveData<NetworkResult<CafeResponse>> get() = _cafe
    override val menuResponse: LiveData<NetworkResult<CafeMenuResponse>> get() = _menu


    override suspend fun handleFetchAllCafes(isLoading: Boolean) {
        performNetworkOperation(
            isShowProgress = !isLoading,
            networkRequest = { remoteApi.fetchCafes() },
            networkResult = _cafe
        )
    }

    override suspend fun handleSyncCafes(cafes: List<CafeModel>) {
        coroutineScope.launch {
            localDao.upsertCafe(
                CafeEntity(Constants.DB_CAFE_LIST_KEY, cafes)
            )
        }
    }

    override suspend fun handleGetCafe(id: String): CafeEntity {
        return coroutineScope.async {
            localDao.getCafe(id)
        }.await()
    }

    override suspend fun handleFetchMenu(isLoading: Boolean, cafeId: Int) {
        performNetworkOperation(
            isShowProgress = !isLoading,
            networkRequest = { remoteApi.fetchMenu(cafeId) },
            networkResult = _menu
        )
    }

    override suspend fun handleSyncMenu(menu: MenuModel) {
        coroutineScope.launch {
            localDao.upsertMenu(
                MenuEntity(menu.cafeId, menu.beverageCategories)
            )
        }
    }

    override suspend fun handleGetMenu(cafeId: Int): MenuEntity {
        return coroutineScope.async {
            localDao.getMenu(cafeId)
        }.await()
    }

}