package kind.sun.dev.coffeeworld.ui.adapter.cafe

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.databinding.ItemCafeShopBinding
import kind.sun.dev.coffeeworld.databinding.ItemCafeShopTitleBinding
import kind.sun.dev.coffeeworld.util.helper.view.setOnClickScaleListener

sealed class CafeShopViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    var onItemClickListener: ((CafeModel) -> Unit)? = null

    class TitleShopViewHolder(private val binding: ItemCafeShopTitleBinding): CafeShopViewHolder(binding) {

        fun bindView(cafeTitle: CafeShopViewItem.Title) = binding.apply {
            model = cafeTitle
            executePendingBindings()
        }
    }

    class ItemShopViewHolder(private val binding: ItemCafeShopBinding): CafeShopViewHolder(binding) {

        fun bindView(cafeItem: CafeShopViewItem.Shop) = binding.apply {
            cafeItem.let {
                cafe = it.cafe
                executePendingBindings()
                tvDistance.text = it.distance
                root.setOnClickScaleListener {
                    onItemClickListener?.invoke(it.cafe)
                }
            }
        }
    }

}