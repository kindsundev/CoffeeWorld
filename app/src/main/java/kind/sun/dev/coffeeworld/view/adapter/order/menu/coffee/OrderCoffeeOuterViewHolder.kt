package kind.sun.dev.coffeeworld.view.adapter.order.menu.coffee

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kind.sun.dev.coffeeworld.data.local.model.DrinkModel
import kind.sun.dev.coffeeworld.databinding.ItemOrderCollectionBinding
import kind.sun.dev.coffeeworld.view.adapter.order.menu.OrderMenuViewItem

internal class OrderCoffeeOuterViewHolder(
    private val binding: ItemOrderCollectionBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val coffeeAdapter by lazy { OrderCoffeeInnerAdapter() }

    internal fun setNestedScrolling(state: Boolean) {
        binding.rvItems.isNestedScrollingEnabled = state
    }

    internal fun bindView(
        coffees: OrderMenuViewItem.Coffees,
        onItemClickListener: ((type : (String), id: (Int), drink: (DrinkModel)?) -> Unit)? = null
    ) {
        initView(coffees)
        coffeeAdapter.items = coffees.drinks
        coffeeAdapter.onItemClickListener = onItemClickListener
    }

    private fun initView(coffees: OrderMenuViewItem.Coffees) = binding.apply {
        tvTitle.text = coffees.categoryName
        rvItems.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = coffeeAdapter
            hasFixedSize()
        }
    }

}