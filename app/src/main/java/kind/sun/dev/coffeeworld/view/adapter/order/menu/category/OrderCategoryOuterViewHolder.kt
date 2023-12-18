package kind.sun.dev.coffeeworld.view.adapter.order.menu.category

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kind.sun.dev.coffeeworld.data.local.model.DrinkModel
import kind.sun.dev.coffeeworld.databinding.ItemOrderCategoryBinding
import kind.sun.dev.coffeeworld.view.adapter.order.menu.OrderMenuViewItem

internal class OrderCategoryOuterViewHolder(
    private val binding: ItemOrderCategoryBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val categoryAdapter by lazy { OrderCategoryInnerAdapter() }

    internal fun bindView(
        items: OrderMenuViewItem.Categories,
        onItemClickListener: ((type : (String), id: (Int), drink: (DrinkModel)?) -> Unit)? = null
    ) {
        setupRecyclerView()
        categoryAdapter.items = items.categories
        categoryAdapter.onItemClickListener = onItemClickListener
    }

    private fun setupRecyclerView() = binding.rvCategory.apply {
        layoutManager = GridLayoutManager(binding.root.context, 4)
        adapter = categoryAdapter
        hasFixedSize()
    }
}