package kind.sun.dev.coffeeworld.view.adapter.order.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kind.sun.dev.coffeeworld.data.local.model.DrinkModel
import kind.sun.dev.coffeeworld.databinding.ItemOrderCategoryBinding
import kind.sun.dev.coffeeworld.databinding.ItemOrderCollectionBinding
import kind.sun.dev.coffeeworld.utils.common.Logger
import kind.sun.dev.coffeeworld.view.adapter.order.menu.banner.OrderBannerOuterViewHolder
import kind.sun.dev.coffeeworld.view.adapter.order.menu.category.OrderCategoryOuterViewHolder
import kind.sun.dev.coffeeworld.view.adapter.order.menu.coffee.OrderCoffeeOuterViewHolder


class OrderMenuAdapter(
    private val menu: List<OrderMenuViewItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val NO_POSITION = -1
        const val CATEGORY_TYPE = 0
        const val BANNER_TYPE = 1
        const val COFFEE_TYPE = 2
    }

    var onItemClickListener: ((type : (String), id: (Int), drink: (DrinkModel)?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            CATEGORY_TYPE -> {
                OrderCategoryOuterViewHolder(
                    ItemOrderCategoryBinding.inflate(layoutInflater, parent, false)
                )
            }
            BANNER_TYPE -> {
                OrderBannerOuterViewHolder(
                    ItemOrderCollectionBinding.inflate(layoutInflater, parent, false)
                )
            }
            COFFEE_TYPE -> {
                OrderCoffeeOuterViewHolder(
                    ItemOrderCollectionBinding.inflate(layoutInflater, parent, false)
                )
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun getItemCount(): Int = menu.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val items = menu[position]
        when(holder) {
            is OrderCategoryOuterViewHolder -> {
                holder.bindView(items as OrderMenuViewItem.Categories, onItemClickListener)
            }
            is OrderBannerOuterViewHolder -> {
                holder.bindView(items as OrderMenuViewItem.Banners, onItemClickListener)
            }
            is OrderCoffeeOuterViewHolder -> {
                holder.bindView(items as OrderMenuViewItem.Coffees, onItemClickListener)
                holder.setNestedScrolling(false)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(menu[position]) {
            is OrderMenuViewItem.Categories -> CATEGORY_TYPE
            is OrderMenuViewItem.Banners -> BANNER_TYPE
            is OrderMenuViewItem.Coffees -> COFFEE_TYPE
            else -> -1
        }
    }

    fun getCoffeesPositionByCategoryId(id: Int): Int {
        for ((index, item) in menu.withIndex()) {
            if (item is OrderMenuViewItem.Coffees && item.categoryId == id) {
                Logger.warning("${item.categoryName}")
                return index
            }
        }
        return RecyclerView.NO_POSITION
    }

}