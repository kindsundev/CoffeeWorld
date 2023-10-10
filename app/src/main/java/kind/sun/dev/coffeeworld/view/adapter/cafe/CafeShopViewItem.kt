package kind.sun.dev.coffeeworld.view.adapter.cafe

import kind.sun.dev.coffeeworld.data.model.response.cafe.CafeModel
import kind.sun.dev.coffeeworld.utils.dataset.CafeShopDataSet

sealed class CafeShopViewItem {

    data class Title(val title: String): CafeShopViewItem()

    data class ItemShop(
        val type: CafeShopDataSet.Id,
        val cafe: CafeModel,
        val distance: String
    ) : CafeShopViewItem()

}