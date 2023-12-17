package kind.sun.dev.coffeeworld.view.adapter.order.menu.coffee

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kind.sun.dev.coffeeworld.data.local.model.DrinkModel
import kind.sun.dev.coffeeworld.databinding.ItemOrderCollectionCoffeeBinding
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.helper.view.setOnClickScaleListener
import kind.sun.dev.coffeeworld.view.adapter.BaseDiffUtil

internal class OrderCoffeeInnerAdapter : RecyclerView.Adapter<OrderCoffeeInnerAdapter.CoffeeViewHolder>() {

    internal var items = listOf<DrinkModel>()
        set(value) {
            DiffUtil.calculateDiff(BaseDiffUtil(field, value)).dispatchUpdatesTo(this)
            field = value
        }

    internal var onItemClickListener: ((type : (String), id: (Int)) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeViewHolder {
        return CoffeeViewHolder(
            ItemOrderCollectionCoffeeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CoffeeViewHolder, position: Int) {
        if (items.isEmpty()) return
        holder.bindView(items[position])
    }

    inner class CoffeeViewHolder(
        private val binding: ItemOrderCollectionCoffeeBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: DrinkModel) = binding.apply {
            drink = item
            root.setOnClickScaleListener {
                onItemClickListener?.invoke(Constants.ORDER_COFFEE_ROOT_EVENT, item.id)
            }
            fabAdd.setOnClickScaleListener {
                onItemClickListener?.invoke(Constants.ORDER_COFFEE_FAB_EVENT, item.id)
            }
            executePendingBindings()
        }

    }

}