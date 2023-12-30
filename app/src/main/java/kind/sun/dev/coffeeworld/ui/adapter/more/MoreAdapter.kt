package kind.sun.dev.coffeeworld.ui.adapter.more

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kind.sun.dev.coffeeworld.base.BaseDiffUtil
import kind.sun.dev.coffeeworld.databinding.ItemMoreBoxDoubleBinding
import kind.sun.dev.coffeeworld.databinding.ItemMoreBoxOnlyBinding
import kind.sun.dev.coffeeworld.databinding.ItemMoreRowBinding
import kind.sun.dev.coffeeworld.databinding.ItemMoreTitleBinding
import kind.sun.dev.coffeeworld.util.dataset.MoreDataSet
import kind.sun.dev.coffeeworld.util.helper.view.inflateBinding

class MoreAdapter : RecyclerView.Adapter<MoreViewHolder>() {

    var onItemClickListener: ((itemId: MoreDataSet.Id) -> Unit)? = null

    var items = listOf<MoreViewItem>()
        set(value) {
            BaseDiffUtil.calculateDiff(field, value, this)
            field = value
        }

    companion object {
        const val ROW_TYPE = 0
        const val BOX_ONLY_TYPE = 1
        const val BOX_DOUBLE_TYPE = 2
        const val TITLE_TYPE = 4
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreViewHolder {
        return when(viewType) {
            TITLE_TYPE -> {
                MoreViewHolder.TitleViewHolder(parent.inflateBinding(ItemMoreTitleBinding::inflate))
            }
            BOX_ONLY_TYPE -> {
                MoreViewHolder.OnlyBoxViewHolder(parent.inflateBinding(ItemMoreBoxOnlyBinding::inflate))
            }
            BOX_DOUBLE_TYPE -> {
                MoreViewHolder.DoubleBoxViewHolder(parent.inflateBinding(ItemMoreBoxDoubleBinding::inflate))
            }
            ROW_TYPE -> {
                MoreViewHolder.RowViewHolder(parent.inflateBinding(ItemMoreRowBinding::inflate))
            }
            else -> throw IllegalStateException("Invalid view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: MoreViewHolder, position: Int) {
        val item = items[position]
        holder.onItemClickListener = onItemClickListener
        when (holder) {
            is MoreViewHolder.TitleViewHolder -> {
                holder.bindView(item as MoreViewItem.Title)
            }
            is MoreViewHolder.OnlyBoxViewHolder -> {
                holder.bindView(item as MoreViewItem.Item)
            }
            is MoreViewHolder.DoubleBoxViewHolder -> {
                holder.bindView((item as MoreViewItem.Items).items)
            }
            is MoreViewHolder.RowViewHolder -> {
                holder.bindView(item as MoreViewItem.Item)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when(val item = items[position]) {
            is MoreViewItem.Title -> TITLE_TYPE
            is MoreViewItem.Items -> BOX_DOUBLE_TYPE
            is MoreViewItem.Item -> item.type
        }
    }
}