package kind.sun.dev.coffeeworld.view.adapter.order.menu

import kind.sun.dev.coffeeworld.data.local.model.CategoryModel
import kind.sun.dev.coffeeworld.data.local.model.DrinkModel
import kind.sun.dev.coffeeworld.utils.dataset.OrderDataSet

sealed class OrderMenuViewItem {

    data class Banners(
        val items: List<BannerItem> = OrderDataSet.getBannerItem()
    ): OrderMenuViewItem()

    data class Categories(
        val categories: List<CategoryModel>
    ): OrderMenuViewItem()

    data class Coffees(
        val categoryId: Int,
        val categoryName: String,
        val drinks: List<DrinkModel>
    ): OrderMenuViewItem()
}

data class BannerItem(
    val id: Int,
    val image: Int
): OrderMenuViewItem()
