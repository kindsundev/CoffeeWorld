package kind.sun.dev.coffeeworld.contract

import kind.sun.dev.coffeeworld.data.model.response.cafe.CafeModel
import kind.sun.dev.coffeeworld.view.adapter.cafe.CafeShopViewItem

interface CafeContract {

    interface ViewModel {
        fun getCafeList()

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