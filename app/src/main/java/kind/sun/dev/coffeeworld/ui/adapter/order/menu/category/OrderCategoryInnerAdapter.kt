package kind.sun.dev.coffeeworld.ui.adapter.order.menu.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kind.sun.dev.coffeeworld.base.BaseDiffUtil
import kind.sun.dev.coffeeworld.data.local.model.CategoryModel
import kind.sun.dev.coffeeworld.data.local.model.DrinkModel
import kind.sun.dev.coffeeworld.databinding.ItemCategoryDefaultBinding
import kind.sun.dev.coffeeworld.databinding.ItemCategoryMoreBinding
import kind.sun.dev.coffeeworld.util.common.Constants
import kind.sun.dev.coffeeworld.util.helper.view.setOnClickScaleListener

internal class OrderCategoryInnerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var items = listOf<CategoryModel>()
        set(value) {
            BaseDiffUtil.calculateDiff(field, value, this)
            field = value
        }

    internal var onItemClickListener: ((type : (String), id: (Int), drink: (DrinkModel)?) -> Unit)? = null


    companion object {
        const val DEFAULT_TYPE = 0
        const val MORE_TYPE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == DEFAULT_TYPE) {
            CategoryDefaultViewHolder(ItemCategoryDefaultBinding.inflate(layoutInflater, parent, false))
        } else {
            CategoryMoreViewHolder(ItemCategoryMoreBinding.inflate(layoutInflater, parent, false))
        }
    }

    override fun getItemCount(): Int {
        if (items.size in 4..7) return 4
        if (items.size >= 8) return 8
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 7) MORE_TYPE else DEFAULT_TYPE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (items.isEmpty()) return
        if (holder is CategoryDefaultViewHolder) {
            holder.bindView(items[position])
        }
        if (holder is CategoryMoreViewHolder) {
            holder.bindView()
        }
    }

    inner class CategoryDefaultViewHolder(
        private val binding: ItemCategoryDefaultBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        internal fun bindView(item: CategoryModel) = binding.apply {
            category = item
            root.setOnClickScaleListener {
                onItemClickListener?.invoke(Constants.ORDER_CATEGORY_EVENT, item.id, null)
            }
            executePendingBindings()
        }
    }

    inner class CategoryMoreViewHolder(
        private val binding: ItemCategoryMoreBinding
    ): RecyclerView.ViewHolder(binding.root) {

        internal fun bindView() = binding.root.setOnClickScaleListener {
            onItemClickListener?.invoke(
                Constants.ORDER_CATEGORY_EVENT, Constants.CATEGORY_MORE_ID, null
            )
        }

    }
}