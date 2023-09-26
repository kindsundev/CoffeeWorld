package kind.sun.dev.coffeeworld.ui.more.adapter

import kind.sun.dev.coffeeworld.utils.data.MoreOptionUtils

sealed class MoreRecyclerViewItem {

    companion object {
        const val ROW_TYPE = 0
        const val BOX_ONLY_TYPE = 1
        const val BOX_DOUBLE_TYPE = 2
    }

    data class Title(
        val resTitle: Int
    ) : MoreRecyclerViewItem()

    data class Item(
        val id: MoreOptionUtils.Id,
        val resLogo: Int,
        val resName: Int,
        val type: Int
    ) : MoreRecyclerViewItem()

    data object Temp : MoreRecyclerViewItem()
}