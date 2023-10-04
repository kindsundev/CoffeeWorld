package kind.sun.dev.coffeeworld.view.adapter.more

import kind.sun.dev.coffeeworld.utils.dataset.MoreDataSet

sealed class MoreViewItem {

    companion object {
        const val ROW_TYPE = 0
        const val BOX_ONLY_TYPE = 1
        const val BOX_DOUBLE_TYPE = 2
    }

    data class Title(val resTitle: Int) : MoreViewItem()

    data class Item(
        val id: MoreDataSet.Id,
        val resLogo: Int,
        val resName: Int,
        val type: Int
    ) : MoreViewItem()

    data object Temp : MoreViewItem()
}