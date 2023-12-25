package kind.sun.dev.coffeeworld.ui.adapter.order.menu.banner

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kind.sun.dev.coffeeworld.data.local.model.DrinkModel
import kind.sun.dev.coffeeworld.databinding.ItemOrderCollectionBinding
import kind.sun.dev.coffeeworld.ui.adapter.order.menu.OrderMenuViewItem

internal class OrderBannerOuterViewHolder(
    private val binding: ItemOrderCollectionBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val bannerAdapter by lazy { OrderBannerInnerAdapter() }

    internal fun bindView(
        banners: OrderMenuViewItem.Banners,
        onItemClickListener: ((type : (String), id: (Int), drink: (DrinkModel)?) -> Unit)? = null
    ) {
        initView()
        bannerAdapter.items = banners.items
        bannerAdapter.onItemClickListener = onItemClickListener
    }

    private fun initView() = binding.apply {
        tvTitle.text = "Collection"
        rvItems.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = bannerAdapter
            hasFixedSize()
        }
    }

}