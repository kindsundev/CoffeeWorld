package kind.sun.dev.coffeeworld.view.adapter.order.menu.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.data.local.model.CategoryModel
import kind.sun.dev.coffeeworld.data.local.model.DrinkModel
import kind.sun.dev.coffeeworld.databinding.ItemCategoryCircleBinding
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.helper.view.setOnClickScaleListener
import kind.sun.dev.coffeeworld.view.adapter.BaseDiffUtil

internal class OrderCategoryInnerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var items = listOf<CategoryModel>()
        set(value) {
            DiffUtil.calculateDiff(BaseDiffUtil(field, value)).dispatchUpdatesTo(this)
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
            CategoryDefaultViewHolder(ItemCategoryCircleBinding.inflate(layoutInflater, parent, false))
        } else {
            CategoryMoreViewHolder(ItemCategoryCircleBinding.inflate(layoutInflater, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return if (items.size >= 8) 8 else items.size
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
        private val binding: ItemCategoryCircleBinding
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
        private val binding: ItemCategoryCircleBinding
    ): RecyclerView.ViewHolder(binding.root) {

        internal fun bindView() = binding.apply {
            imgCategory.setBackgroundResource(R.drawable.ic_baseline_more_horiz_24)
            tvName.text = root.context.getString(R.string.more)
            root.setOnClickScaleListener {
                onItemClickListener?.invoke(
                    Constants.ORDER_CATEGORY_EVENT, Constants.CATEGORY_MORE_ID, null
                )
            }
        }

    }
}