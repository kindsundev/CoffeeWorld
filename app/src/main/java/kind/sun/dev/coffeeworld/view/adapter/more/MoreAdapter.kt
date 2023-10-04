package kind.sun.dev.coffeeworld.view.adapter.more

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.databinding.ItemMoreBoxDoubleBinding
import kind.sun.dev.coffeeworld.databinding.ItemMoreBoxOnlyBinding
import kind.sun.dev.coffeeworld.databinding.ItemMoreRowBinding
import kind.sun.dev.coffeeworld.databinding.ItemMoreTempBinding
import kind.sun.dev.coffeeworld.databinding.ItemMoreTitleBinding
import kind.sun.dev.coffeeworld.utils.dataset.MoreDataSet

class MoreAdapter : RecyclerView.Adapter<MoreViewHolder>() {

    private var doubleBoxItems = mutableListOf<MoreViewItem.Item>()

    var onItemClickListener: ((itemId: MoreDataSet.Id) -> Unit)? = null

    var items = listOf<MoreViewItem>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            R.layout.item_more_title -> {
                MoreViewHolder.TitleViewHolder(
                    ItemMoreTitleBinding.inflate(layoutInflater, parent, false)
                )
            }
            R.layout.item_more_box_only -> {
                MoreViewHolder.OnlyBoxViewHolder(
                    ItemMoreBoxOnlyBinding.inflate(layoutInflater, parent, false)
                )
            }
            R.layout.item_more_box_double -> {
                MoreViewHolder.DoubleBoxViewHolder(
                    ItemMoreBoxDoubleBinding.inflate(layoutInflater, parent, false)
                )
            }
            R.layout.item_more_row -> {
                MoreViewHolder.RowViewHolder(
                    ItemMoreRowBinding.inflate(layoutInflater, parent, false)
                )
            }
            else -> {
                MoreViewHolder.TempViewHolder(
                    ItemMoreTempBinding.inflate(layoutInflater, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: MoreViewHolder, position: Int) {
        val item = items[position].also {
            if (it is MoreViewItem.Item && it.type == MoreViewItem.BOX_DOUBLE_TYPE) {
                doubleBoxItems.add(it)
            }
            if (doubleBoxItems.size > 2) doubleBoxItems.clear()
        }

        holder.onItemClickListener = onItemClickListener

        when (holder) {
            is MoreViewHolder.TitleViewHolder -> {
                holder.onBind(item as MoreViewItem.Title)
            }
            is MoreViewHolder.OnlyBoxViewHolder -> {
                holder.onBind(item as MoreViewItem.Item)
            }
            is MoreViewHolder.DoubleBoxViewHolder -> {
                if (doubleBoxItems.size == 2) {
                    holder.onBind(doubleBoxItems[0], doubleBoxItems[1])
                }
            }
            is MoreViewHolder.RowViewHolder -> {
                holder.onBind(item as MoreViewItem.Item)
            }
            else -> {}
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when(val item = items[position]) {
            is MoreViewItem.Title -> R.layout.item_more_title
            is MoreViewItem.Temp -> R.layout.item_more_box_double
            is MoreViewItem.Item -> {
                when (item.type) {
                    MoreViewItem.ROW_TYPE -> R.layout.item_more_row
                    MoreViewItem.BOX_ONLY_TYPE -> R.layout.item_more_box_only
                    else -> { R.layout.item_more_temp }
                }
            }
        }
    }


}