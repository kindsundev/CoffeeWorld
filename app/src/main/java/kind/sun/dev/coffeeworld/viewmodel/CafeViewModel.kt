package kind.sun.dev.coffeeworld.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.contract.CafeContract
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.data.repository.CafeRepository
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.dataset.CafeShopDataSet
import kind.sun.dev.coffeeworld.view.adapter.cafe.CafeShopViewItem
import kind.sun.dev.coffeeworld.base.BaseViewModel
import kind.sun.dev.coffeeworld.data.local.dao.CafeDAO
import kind.sun.dev.coffeeworld.data.remote.response.CafeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CafeViewModel @Inject constructor(
    private val cafeRepository: CafeRepository,
    private val cafeDao: CafeDAO
): BaseViewModel(), CafeContract.ViewModel {
    val cafe: LiveData<NetworkResult<CafeResponse>>
        get() = cafeRepository.cafe

    override fun onFetchAllCafes(
        onDataFromLocal: (List<CafeModel>?) -> Unit,
        onFailedMessage: (String) -> Unit
    ) {
        handleCheckAndRoute(
            conditionChecker = null,
            onPassedCheck = {
                viewModelScope.launch { cafeRepository.fetchAllCafes() }
            },
            onFailedCheck = { reason, localDataRequired ->
                if (localDataRequired) {
                    viewModelScope.launch {
                        onDataFromLocal(withContext(Dispatchers.IO) {
                            cafeDao.getAllCafes()
                        }).also { delay(300) }
                        onFailedMessage(reason)
                    }
                } else {
                    onFailedMessage(reason)
                }
            }
        )
    }

    override suspend fun onSyncAllCafes(cafes: List<CafeModel>){
        viewModelScope.launch {
            withContext(Dispatchers.IO) { cafeDao.upsertAllCafes(cafes) }
        }
    }

    override fun mapCafeModelToCafeViewItem(
        title: Array<String>,
        data: List<CafeModel>
    ): MutableList<CafeShopViewItem> {
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

    override fun filterListByName(name: String, list: List<CafeShopViewItem>): List<CafeShopViewItem> {
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