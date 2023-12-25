package kind.sun.dev.coffeeworld.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class BaseDiffUtil<T>(
    private val oldList: List<T>,
    private val newList: List<T>
): DiffUtil.Callback() {

    companion object {
        fun <T> calculateDiff(
            oldList: List<T>,
            newList: List<T>,
            adapter: RecyclerView.Adapter<*>
        ) {
            DiffUtil.calculateDiff(BaseDiffUtil(oldList, newList)).dispatchUpdatesTo(adapter)
        }
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int  = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
