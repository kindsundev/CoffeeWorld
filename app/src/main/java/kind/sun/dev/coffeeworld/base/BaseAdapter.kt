package kind.sun.dev.coffeeworld.base

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T : Any, VB : ViewDataBinding>
    : RecyclerView.Adapter<BaseAdapter<T, VB>.BaseViewHolder>() {

    protected var items = mutableListOf<T>()
        private set

    protected fun addAll(data: List<T>) {
        BaseDiffUtil.calculateDiff(items, data, this)
    }

    protected fun addItem(item: T, position: Int) {
        items.add(position, item)
        notifyItemInserted(position)
    }

    protected fun updateItem(item: T, position: Int) {
        items[position] = item
        notifyItemChanged(position)
    }

    protected fun removeItem(item: T, position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    abstract fun createView(parent: ViewGroup): VB

    abstract fun bindView(binding: VB, item: T)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(createView(parent))
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        bindView(holder.binding, items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class BaseViewHolder(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}






