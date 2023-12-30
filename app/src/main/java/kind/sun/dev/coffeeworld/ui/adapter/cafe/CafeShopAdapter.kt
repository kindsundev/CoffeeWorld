package kind.sun.dev.coffeeworld.ui.adapter.cafe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kind.sun.dev.coffeeworld.base.BaseDiffUtil
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.databinding.ItemCafeShopBinding
import kind.sun.dev.coffeeworld.databinding.ItemCafeShopTitleBinding
import kind.sun.dev.coffeeworld.util.helper.view.inflateBinding
import java.lang.IllegalArgumentException

class CafeShopAdapter(
    private val onItemCLickListener: ((CafeModel) -> Unit)? = null
): RecyclerView.Adapter<CafeShopViewHolder>() {

    var items = listOf<CafeShopViewItem>()
        set(value) {
            BaseDiffUtil.calculateDiff(field, value, this)
            field = value
        }

    companion object {
        const val TITLE_TYPE = 1
        const val SHOP_TYPE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeShopViewHolder {
        return when (viewType) {
            SHOP_TYPE -> {
                CafeShopViewHolder.ItemShopViewHolder(parent.inflateBinding(ItemCafeShopBinding::inflate))
            }
            TITLE_TYPE -> {
                CafeShopViewHolder.TitleShopViewHolder(parent.inflateBinding(ItemCafeShopTitleBinding::inflate))
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: CafeShopViewHolder, position: Int) {
        holder.onItemClickListener = onItemCLickListener
        when(holder) {
            is CafeShopViewHolder.TitleShopViewHolder -> {
                holder.bindView(items[position] as CafeShopViewItem.Title)
            }
            is CafeShopViewHolder.ItemShopViewHolder -> {
                holder.bindView(items[position] as CafeShopViewItem.Shop)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is CafeShopViewItem.Title -> TITLE_TYPE
            is CafeShopViewItem.Shop -> SHOP_TYPE
        }
    }
}