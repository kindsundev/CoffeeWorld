package kind.sun.dev.coffeeworld.ui.adapter.order.category

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kind.sun.dev.coffeeworld.data.local.model.CategoryModel
import kind.sun.dev.coffeeworld.databinding.ItemCategoryDefaultBinding
import kind.sun.dev.coffeeworld.util.helper.view.inflateBinding
import kind.sun.dev.coffeeworld.util.helper.view.setOnClickScaleListener

class OrderCategoryAdapter(
    val items: List<CategoryModel>,
    val onItemClickListener: (Int) -> Unit
): RecyclerView.Adapter<OrderCategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(parent.inflateBinding(ItemCategoryDefaultBinding::inflate))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    inner class CategoryViewHolder(
        private val binding: ItemCategoryDefaultBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: CategoryModel) = binding.apply {
            category = item
            root.setOnClickScaleListener { onItemClickListener(item.id) }
            executePendingBindings()
        }
    }
}