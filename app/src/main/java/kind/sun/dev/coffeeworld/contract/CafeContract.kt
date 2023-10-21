package kind.sun.dev.coffeeworld.contract

import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.view.adapter.cafe.CafeShopViewItem

interface CafeContract {

    interface ViewModel {
        fun onFetchAllCafes(
            onDataFromLocal: (List<CafeModel>?) -> Unit,
            onFailedMessage: (String) -> Unit
        )

        suspend fun onSyncAllCafes(cafes: List<CafeModel>)

        fun mapCafeModelToCafeViewItem(
            title: Array<String>,
            data: List<CafeModel>
        ): MutableList<CafeShopViewItem>

        fun filterListByName(
            name: String,
            list: List<CafeShopViewItem>
        ): List<CafeShopViewItem>
    }

}