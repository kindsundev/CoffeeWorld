package kind.sun.dev.coffeeworld.view.adapter.order.menu.banner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kind.sun.dev.coffeeworld.data.local.model.DrinkModel
import kind.sun.dev.coffeeworld.databinding.ItemOrderCollectionBannerBinding
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.helper.view.setOnClickScaleListener
import kind.sun.dev.coffeeworld.view.adapter.BaseDiffUtil
import kind.sun.dev.coffeeworld.view.adapter.order.menu.BannerItem

internal class OrderBannerInnerAdapter : RecyclerView.Adapter<OrderBannerInnerAdapter.BannerViewHolder>() {

    internal var items = listOf<BannerItem>()
        set(value) {
            DiffUtil.calculateDiff(BaseDiffUtil(field, value)).dispatchUpdatesTo(this)
            field = value
        }

    internal var onItemClickListener: ((type : (String), id: (Int), drink: (DrinkModel)?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder(
            ItemOrderCollectionBannerBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        if (items.isEmpty()) return
        holder.bindView(items[position])
    }

    inner class BannerViewHolder(
        private val binding: ItemOrderCollectionBannerBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: BannerItem) = binding.apply {
            imgBanner.setImageResource(item.image)
            root.setOnClickScaleListener {
                onItemClickListener?.invoke(Constants.ORDER_BANNER_EVENT, item.id, null)
            }
        }
    }
}