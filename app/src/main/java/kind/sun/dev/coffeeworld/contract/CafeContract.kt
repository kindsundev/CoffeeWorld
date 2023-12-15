package kind.sun.dev.coffeeworld.contract

import androidx.lifecycle.LiveData
import kind.sun.dev.coffeeworld.data.local.entity.CafeEntity
import kind.sun.dev.coffeeworld.data.local.entity.MenuEntity
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.data.local.model.MenuModel
import kind.sun.dev.coffeeworld.data.remote.response.CafeMenuResponse
import kind.sun.dev.coffeeworld.data.remote.response.CafeResponse
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.view.adapter.cafe.CafeShopViewItem

interface CafeContract {

    interface ViewModel {
        suspend fun onFetchAllCafes(
            isLoading: Boolean,
            onDataFromLocal: ((List<CafeModel>?) -> Unit)? = null,
            onFailedMessage: ((String) -> Unit)? = null
        )

        suspend fun onRetrieveAllCafes(): List<CafeModel>?

        suspend fun onSyncAllCafes(cafes: List<CafeModel>)

        suspend fun onFetchMenu(
            isLoading: Boolean,
            cafeId: Int,
            onDataFromLocal: ((MenuModel?) -> Unit)? = null,
            onFailedMessage: ((String) -> Unit)? = null
        )

        suspend fun onRetrieveMenu(cafeId: Int): MenuModel?

        suspend fun onSyncMenu(menu: MenuModel)

        fun convertToCafeViewItem(title: Array<String>, data: List<CafeModel>): MutableList<CafeShopViewItem>

        fun filterCafeList(name: String, list: List<CafeShopViewItem>): List<CafeShopViewItem>
    }


    interface Service {
        val cafeResponse: LiveData<NetworkResult<CafeResponse>>
        val menuResponse: LiveData<NetworkResult<CafeMenuResponse>>

        suspend fun handleFetchAllCafes(isLoading: Boolean)

        suspend fun handleSyncCafes(cafes: List<CafeModel>)

        suspend fun handleGetCafe(id : String): CafeEntity?

        suspend fun handleFetchMenu(isLoading: Boolean, cafeId: Int)

        suspend fun handleSyncMenu(menu: MenuModel)

        suspend fun handleGetMenu(cafeId: Int): MenuEntity?
    }

}