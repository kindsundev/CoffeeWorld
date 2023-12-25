package kind.sun.dev.coffeeworld.ui.adapter.cafe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.base.BaseDiffUtil
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.databinding.ItemCafeShopBinding
import kind.sun.dev.coffeeworld.databinding.ItemCafeShopTitleBinding
import java.lang.IllegalArgumentException

class CafeShopAdapter(
    private val onItemCLickListener: ((CafeModel) -> Unit)? = null
): RecyclerView.Adapter<CafeShopViewHolder>() {

    var items = listOf<CafeShopViewItem>()
        set(value) {
            BaseDiffUtil.calculateDiff(field, value, this)
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeShopViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_cafe_shop -> {
                CafeShopViewHolder.ItemShopViewHolder(
                    ItemCafeShopBinding.inflate(layoutInflater, parent, false)
                )
            }
            R.layout.item_cafe_shop_title -> {
                CafeShopViewHolder.TitleShopViewHolder(
                    ItemCafeShopTitleBinding.inflate(layoutInflater, parent, false)
                )
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: CafeShopViewHolder, position: Int) {
        holder.onItemClickListener = onItemCLickListener
        when(holder) {
            is CafeShopViewHolder.TitleShopViewHolder -> {
                holder.onBind(items[position] as CafeShopViewItem.Title)
            }
            is CafeShopViewHolder.ItemShopViewHolder -> {
                holder.onBind(items[position] as CafeShopViewItem.ItemShop)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is CafeShopViewItem.Title -> R.layout.item_cafe_shop_title
            is CafeShopViewItem.ItemShop -> R.layout.item_cafe_shop
        }
    }
}