package kind.sun.dev.coffeeworld.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.contract.CafeContract
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.dataset.CafeShopDataSet
import kind.sun.dev.coffeeworld.view.adapter.cafe.CafeShopViewItem
import kind.sun.dev.coffeeworld.base.BaseViewModel
import kind.sun.dev.coffeeworld.data.local.model.MenuModel
import kind.sun.dev.coffeeworld.data.remote.response.CafeResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CafeViewModel @Inject constructor(
    private val cafeService: CafeContract.Service,
): BaseViewModel(), CafeContract.ViewModel {
    val cafe: LiveData<NetworkResult<CafeResponse>> get() = cafeService.cafeResponse

    override suspend fun onFetchAllCafes(
        onDataFromLocal: (List<CafeModel>?) -> Unit,
        onFailedMessage: (String) -> Unit
    ) {
        handleCheckAndRoute(
            conditionChecker = null,
            onPassedCheck = { viewModelScope.launch { cafeService.handleFetchAllCafes() } },
            onFailedCheck = { reason, localDataRequired ->
                if (localDataRequired) {
                    viewModelScope.launch {
                        onDataFromLocal(cafeService.handleGetAllCafes())
                        delay(300)
                        onFailedMessage(reason)
                    }
                } else {
                    onFailedMessage(reason)
                }
            }
        )
    }

    override suspend fun onSyncAllCafes(cafes: List<CafeModel>) {
        cafeService.handleSyncAllCafes(cafes)
    }

    override suspend fun onFetchMenuForCafe(cafeId: Int) {

    }

    override suspend fun onSyncMenuOfCafe(menus: List<MenuModel>) {

    }

    override fun convertToCafeViewItem(title: Array<String>, data: List<CafeModel>): MutableList<CafeShopViewItem> {
        var randomDistance: String
        return mutableListOf<CafeShopViewItem>().apply {
            data.forEachIndexed { index, cafeModel ->
                randomDistance = CafeShopDataSet.generateRandomDistance(index + 1)
                if (index == 0) {
                    add(CafeShopViewItem.Title(title[0]))
                    add(CafeShopViewItem.ItemShop(CafeShopDataSet.Id.NEAR_HERE, cafeModel, randomDistance))
                } else {
                    if (index == 1) {
                        add(CafeShopViewItem.Title(title[1]))
                        add(CafeShopViewItem.ItemShop(CafeShopDataSet.Id.NEAR_HERE, cafeModel, randomDistance))
                    } else {
                        add(CafeShopViewItem.ItemShop(CafeShopDataSet.Id.FAR_AWAY, cafeModel, randomDistance))
                    }
                }
            }
        }
    }

    override fun filterCafeList(name: String, list: List<CafeShopViewItem>): List<CafeShopViewItem> {
        val formatName = name.lowercase()
        return mutableListOf<CafeShopViewItem>().apply {
            list.forEach { item ->
                if (item is CafeShopViewItem.ItemShop) {
                    if (item.cafe.name.lowercase().contains(formatName)) {
                        add(item)
                    }
                }
            }
        }
    }


}