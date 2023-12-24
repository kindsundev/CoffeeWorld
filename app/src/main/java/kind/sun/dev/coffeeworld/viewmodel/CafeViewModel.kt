package kind.sun.dev.coffeeworld.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.base.BaseViewModel
import kind.sun.dev.coffeeworld.contract.CafeContract
import kind.sun.dev.coffeeworld.data.local.model.BeverageCategoryModel
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.data.local.model.CategoryModel
import kind.sun.dev.coffeeworld.data.local.model.MenuModel
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.dataset.CafeShopDataSet
import kind.sun.dev.coffeeworld.utils.dataset.OrderDataSet
import kind.sun.dev.coffeeworld.view.adapter.cafe.CafeShopViewItem
import kind.sun.dev.coffeeworld.view.adapter.order.menu.OrderMenuViewItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CafeViewModel @Inject constructor(
    private val cafeService: CafeContract.Service,
): BaseViewModel(), CafeContract.ViewModel {
    val cafe get() = cafeService.cafeResponse
    val menu get() = cafeService.menuResponse

    private val scope = CoroutineScope(Dispatchers.IO)

    override suspend fun onFetchAllCafes(
        isLoading: Boolean,
        onDataFromLocal: ((List<CafeModel>?) -> Unit)?,
        onFailedMessage: ((String) -> Unit)?
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
                            onDataFromLocal?.invoke(it)
                            delay(300)
                            onFailedMessage?.invoke(reason)
                        }
                    }
                } else {
                    onFailedMessage?.invoke(reason)
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
        isLoading: Boolean,
        cafeId: Int,
        onDataFromLocal: ((MenuModel?) -> Unit)?,
        onFailedMessage: ((String) -> Unit)?
    ) {
        handleCheckAndRoute(
            conditionChecker = null,
            onPassedCheck = { viewModelScope.launch { cafeService.handleFetchMenu(isLoading, cafeId) } },
            onFailedCheck = { reason, localDataRequired ->
                if (localDataRequired) {
                    viewModelScope.launch {
                        onRetrieveMenu(cafeId)?.let {
                            onDataFromLocal?.invoke(it)
                            delay(300)
                            onFailedMessage?.invoke(reason)
                        }
                    }
                } else {
                    onFailedMessage?.invoke(reason)
                }
            }
        )
    }

    override suspend fun onRetrieveMenu(cafeId: Int): MenuModel? {
        return cafeService.handleGetMenu(cafeId)?.run {
            MenuModel("", cafeId, beverageCategories)
        }
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

    override fun convertToMenuViewItem(data: List<BeverageCategoryModel>): List<OrderMenuViewItem> {
        val categories = mutableListOf<CategoryModel>()
        val coffees = mutableListOf<OrderMenuViewItem.Coffees>()

        data.forEach { item ->
            categories.add(item.category)
            if (item.drinks.isNotEmpty()) {
                coffees.add(
                    OrderMenuViewItem.Coffees(item.category.id, item.category.name,item.drinks)
                )
            }
        }

        return mutableListOf<OrderMenuViewItem>().apply {
            add(OrderMenuViewItem.Categories(categories))
            add(OrderMenuViewItem.Banners(OrderDataSet.getBannerItem()))
            addAll(coffees)
        }
    }

    override fun filterCafeList(name: String, list: List<CafeShopViewItem>): List<CafeShopViewItem> {
        val formatName = name.lowercase()
        return mutableListOf<CafeShopViewItem>().apply {
            scope.launch {
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
}