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

sealed class MoreRecyclerViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    var onItemClickListener: ((itemId: MoreOptionUtils.Id) -> Unit)? = null

    class TitleViewHolder(private val binding: ItemMoreTitleBinding): MoreRecyclerViewHolder(binding) {
        fun onBind(title: MoreRecyclerViewItem.Title) {
            binding.apply {
                binding.tvTitle.setText(title.resTitle)
            }
        }
    }

    class OnlyBoxViewHolder(private val binding: ItemMoreBoxOnlyBinding): MoreRecyclerViewHolder(binding) {
        fun onBind(data: MoreRecyclerViewItem.Item) {
            binding.apply {
                item = data
                executePendingBindings()
                root.setOnClickListener { onClickMoreItem(it, data) }
            }
        }
    }

    class DoubleBoxViewHolder(private val binding: ItemMoreBoxDoubleBinding): MoreRecyclerViewHolder(binding) {
        fun onBind(firstItem: MoreRecyclerViewItem.Item, secondItem: MoreRecyclerViewItem.Item) {
            binding.apply {
                first = firstItem
                second = secondItem
                executePendingBindings()
                firstOption.setOnClickListener { onClickMoreItem(it, firstItem) }
                secondOption.setOnClickListener { onClickMoreItem(it, secondItem) }
            }
        }
    }

    class RowViewHolder(private val binding: ItemMoreRowBinding): MoreRecyclerViewHolder(binding) {
        fun onBind(data: MoreRecyclerViewItem.Item) {
            binding.apply {
                item = data
                executePendingBindings()
                root.setOnClickListener { onClickMoreItem(it, data) }

                when (data.id) {
                    MoreOptionUtils.Id.USER -> {
                        root.setBackgroundResource(R.drawable.bg_layout_solid_white_border_top_8)
                    }
                    MoreOptionUtils.Id.ORDER_REVIEWS -> {
                        root.setBackgroundResource(R.drawable.bg_layout_solid_white_border_top_8)
                    }
                    MoreOptionUtils.Id.CONTACT_AND_FEEDBACK -> {
                        root.setBackgroundResource(R.drawable.bg_layout_solid_white_border_bottom_8)
                        viewDivider.visibility = View.GONE
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

    class TempViewHolder(binding: ItemMoreTempBinding): MoreRecyclerViewHolder(binding)

    protected fun onClickMoreItem(view: View, option: MoreRecyclerViewItem.Item) {
        view.setScaleAnimation(Constants.DURATION_SHORT, Constants.SCALE_LOW) {
            onItemClickListener?.invoke(option.id)
        }
    }
}