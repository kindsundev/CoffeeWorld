package kind.sun.dev.coffeeworld.ui.adapter.cafe

import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.util.dataset.CafeShopDataSet

sealed class CafeShopViewItem {

    data class Title(val title: String): CafeShopViewItem()

    data class Shop(
        val type: CafeShopDataSet.Id,
        val cafe: CafeModel,
        val distance: String
    ) : CafeShopViewItem()

}