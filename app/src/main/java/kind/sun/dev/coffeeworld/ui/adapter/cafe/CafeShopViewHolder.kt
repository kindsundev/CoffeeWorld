package kind.sun.dev.coffeeworld.ui.adapter.cafe

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.databinding.ItemCafeShopBinding
import kind.sun.dev.coffeeworld.databinding.ItemCafeShopTitleBinding
import kind.sun.dev.coffeeworld.util.helper.animation.setScaleAnimation

sealed class CafeShopViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    var onItemClickListener: ((CafeModel) -> Unit)? = null

    protected fun onClickCafeItem(view: View, cafe: CafeModel) {
        view.setScaleAnimation {
            onItemClickListener?.invoke(cafe)
        }
    }

    class TitleShopViewHolder(private val binding: ItemCafeShopTitleBinding): CafeShopViewHolder(binding) {
        fun onBind(cafeTitle: CafeShopViewItem.Title) = binding.apply {
            model = cafeTitle
            executePendingBindings()
        }
    }

    class ItemShopViewHolder(private val binding: ItemCafeShopBinding): CafeShopViewHolder(binding) {
        fun onBind(cafeItem: CafeShopViewItem.ItemShop) = binding.apply {
            cafeItem.let {
                cafe = it.cafe
                executePendingBindings()
                tvDistance.text = it.distance
                root.setOnClickListener { view -> onClickCafeItem(view, it.cafe) }
            }
        }
    }

}