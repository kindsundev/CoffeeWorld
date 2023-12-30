package kind.sun.dev.coffeeworld.ui.adapter.more

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.databinding.ItemMoreBoxDoubleBinding
import kind.sun.dev.coffeeworld.databinding.ItemMoreBoxOnlyBinding
import kind.sun.dev.coffeeworld.databinding.ItemMoreRowBinding
import kind.sun.dev.coffeeworld.databinding.ItemMoreTitleBinding
import kind.sun.dev.coffeeworld.util.dataset.MoreDataSet
import kind.sun.dev.coffeeworld.util.helper.view.setOnClickScaleListener

sealed class MoreViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    var onItemClickListener: ((itemId: MoreDataSet.Id) -> Unit)? = null

    class TitleViewHolder(private val binding: ItemMoreTitleBinding): MoreViewHolder(binding) {
        fun bindView(title: MoreViewItem.Title) {
            binding.apply {
                binding.tvTitle.setText(title.resTitle)
            }
        }
    }

    class OnlyBoxViewHolder(private val binding: ItemMoreBoxOnlyBinding): MoreViewHolder(binding) {
        fun bindView(data: MoreViewItem.Item) = binding.apply {
            item = data
            root.setOnClickScaleListener { onItemClickListener?.invoke(data.id) }
            executePendingBindings()
        }
    }

    class DoubleBoxViewHolder(private val binding: ItemMoreBoxDoubleBinding): MoreViewHolder(binding) {
        fun bindView(items: List<MoreViewItem.Item>) = binding.apply {
            if (items[0] == null) return@apply
            first = items[0]
            second = items[1]
            firstOption.setOnClickScaleListener { onItemClickListener?.invoke(items[0].id) }
            secondOption.setOnClickScaleListener { onItemClickListener?.invoke(items[1].id) }
            executePendingBindings()
        }
    }

    class RowViewHolder(private val binding: ItemMoreRowBinding): MoreViewHolder(binding) {
        fun bindView(data: MoreViewItem.Item) {
            binding.apply {
                item = data
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
                root.setOnClickScaleListener { onItemClickListener?.invoke(data.id) }
                executePendingBindings()
            }
        }
    }
}