package kind.sun.dev.coffeeworld.ui.adapter.more

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.databinding.ItemMoreBoxDoubleBinding
import kind.sun.dev.coffeeworld.databinding.ItemMoreBoxOnlyBinding
import kind.sun.dev.coffeeworld.databinding.ItemMoreRowBinding
import kind.sun.dev.coffeeworld.databinding.ItemMoreTempBinding
import kind.sun.dev.coffeeworld.databinding.ItemMoreTitleBinding
import kind.sun.dev.coffeeworld.util.helper.animation.setScaleAnimation
import kind.sun.dev.coffeeworld.util.dataset.MoreDataSet

sealed class MoreViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    var onItemClickListener: ((itemId: MoreDataSet.Id) -> Unit)? = null

    protected fun onClickMoreItem(view: View, optionId: MoreDataSet.Id) {
        view.setScaleAnimation {
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
                    MoreDataSet.Id.ORDER_REVIEWS -> {
                        root.setBackgroundResource(R.drawable.bg_layout_solid_white_border_top_8)
                    }
                    MoreDataSet.Id.CONTACT_AND_FEEDBACK -> {
                        root.setBackgroundResource(R.drawable.bg_layout_solid_white_border_bottom_8)
                        viewDivider.visibility = View.GONE
                    }
                    MoreDataSet.Id.USER -> {
                        root.setBackgroundResource(R.drawable.bg_layout_solid_white_border_top_8)
                    }
                    MoreDataSet.Id.PAYMENT -> {
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