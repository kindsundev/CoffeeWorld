package kind.sun.dev.coffeeworld.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.base.BaseViewModel
import kind.sun.dev.coffeeworld.contract.CafeContract
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.data.local.model.MenuModel
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.dataset.CafeShopDataSet
import kind.sun.dev.coffeeworld.view.adapter.cafe.CafeShopViewItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CafeViewModel @Inject constructor(
    private val cafeService: CafeContract.Service,
): BaseViewModel(), CafeContract.ViewModel {
    val cafe get() = cafeService.cafeResponse
    val menu get() = cafeService.menuResponse

    override suspend fun onFetchAllCafes(
        isLoading: Boolean,
        onDataFromLocal: (List<CafeModel>?) -> Unit,
        onFailedMessage: (String) -> Unit
    ) {
        handleCheckAndRoute(
            conditionChecker = null,
            onPassedCheck = {
                viewModelScope.launch { cafeService.handleFetchAllCafes(isLoading) }
            },
            onFailedCheck = { reason, localDataRequired ->
                if (localDataRequired) {
                    viewModelScope.launch {
                        onRetrieveAllCafes()?.let {
                            onDataFromLocal(it)
                            delay(300)
                            onFailedMessage(reason)
                        }
                    }
                } else {
                    onFailedMessage(reason)
                }
            }
        )
    }

    override suspend fun onRetrieveAllCafes(): List<CafeModel>? {
        return cafeService.handleGetCafe(Constants.DB_CAFE_LIST_KEY)?.items
    }

    override suspend fun onSyncAllCafes(cafes: List<CafeModel>) {
        cafeService.handleSyncCafes(cafes)
    }

    override suspend fun onFetchMenu(
        cafeId: Int,
        onDataFromLocal: (MenuModel?) -> Unit,
        onFailedMessage: (String) -> Unit
    ) {
        handleCheckAndRoute(
            conditionChecker = null,
            onPassedCheck = { viewModelScope.launch { cafeService.handleFetchMenu(cafeId) } },
            onFailedCheck = { reason, localDataRequired ->
                if (localDataRequired) {
                    viewModelScope.launch {
                        cafeService.handleGetMenu(cafeId)?.let {
                            onDataFromLocal.invoke(MenuModel("", it.cafeId, it.beverageCategories))
                            delay(300)
                            onFailedMessage(reason)
                        }
                    }
                } else {
                    onFailedMessage(reason)
                }
            }
        )
    }

    override suspend fun onSyncMenu(menu: MenuModel) {
        cafeService.handleSyncMenu(menu)
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