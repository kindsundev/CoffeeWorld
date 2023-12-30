package kind.sun.dev.coffeeworld.ui.adapter.more

import kind.sun.dev.coffeeworld.util.dataset.MoreDataSet

sealed class MoreViewItem {

    data class Title(val resTitle: Int) : MoreViewItem()

    data class Item(
        val id: MoreDataSet.Id,
        val resLogo: Int,
        val resName: Int,
        val type: Int,
    ) : MoreViewItem()

    data class Items(
        val items: List<Item>
    ) : MoreViewItem()
}