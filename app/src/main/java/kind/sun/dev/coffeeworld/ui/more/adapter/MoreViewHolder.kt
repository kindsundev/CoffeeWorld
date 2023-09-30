package kind.sun.dev.coffeeworld.ui.more.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.databinding.ItemMoreBoxDoubleBinding
import kind.sun.dev.coffeeworld.databinding.ItemMoreBoxOnlyBinding
import kind.sun.dev.coffeeworld.databinding.ItemMoreRowBinding
import kind.sun.dev.coffeeworld.databinding.ItemMoreTempBinding
import kind.sun.dev.coffeeworld.databinding.ItemMoreTitleBinding
import kind.sun.dev.coffeeworld.utils.animation.setScaleAnimation
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.data.MoreOptionUtils

sealed class MoreViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    var onItemClickListener: ((itemId: MoreOptionUtils.Id) -> Unit)? = null

    protected fun onClickMoreItem(view: View, optionId: MoreOptionUtils.Id) {
        view.setScaleAnimation(Constants.DURATION_SHORT, Constants.SCALE_LOW) {
            onItemClickListener?.invoke(optionId)
        }
    }

    class TitleViewHolder(private val binding: ItemMoreTitleBinding): MoreViewHolder(binding) {
        fun onBind(title: MoreViewItem.Title) {
            binding.apply {
                binding.tvTitle.setText(title.resTitle)
            }
        }
    }

    class OnlyBoxViewHolder(private val binding: ItemMoreBoxOnlyBinding): MoreViewHolder(binding) {
        fun onBind(data: MoreViewItem.Item) {
            binding.apply {
                item = data
                executePendingBindings()
                root.setOnClickListener { onClickMoreItem(it, data.id) }
            }
        }
    }

    class DoubleBoxViewHolder(private val binding: ItemMoreBoxDoubleBinding): MoreViewHolder(binding) {
        fun onBind(firstItem: MoreViewItem.Item, secondItem: MoreViewItem.Item) {
            binding.apply {
                first = firstItem
                second = secondItem
                executePendingBindings()
                firstOption.setOnClickListener { onClickMoreItem(it, firstItem.id) }
                secondOption.setOnClickListener { onClickMoreItem(it, secondItem.id) }
            }
        }
    }

    class RowViewHolder(private val binding: ItemMoreRowBinding): MoreViewHolder(binding) {
        fun onBind(data: MoreViewItem.Item) {
            binding.apply {
                item = data
                executePendingBindings()
                root.setOnClickListener { onClickMoreItem(it, data.id) }

                when (data.id) {
                    MoreOptionUtils.Id.ORDER_REVIEWS -> {
                        root.setBackgroundResource(R.drawable.bg_layout_solid_white_border_top_8)
                    }
                    MoreOptionUtils.Id.CONTACT_AND_FEEDBACK -> {
                        root.setBackgroundResource(R.drawable.bg_layout_solid_white_border_bottom_8)
                        viewDivider.visibility = View.GONE
                    }
                    MoreOptionUtils.Id.USER -> {
                        root.setBackgroundResource(R.drawable.bg_layout_solid_white_border_top_8)
                    }
                    MoreOptionUtils.Id.PAYMENT -> {
                        root.setBackgroundResource(R.drawable.bg_layout_solid_white_border_bottom_8)
                        viewDivider.visibility = View.GONE
                    }
                    else -> {}
                }
            }
        }
    }

    class TempViewHolder(binding: ItemMoreTempBinding): MoreViewHolder(binding)

}