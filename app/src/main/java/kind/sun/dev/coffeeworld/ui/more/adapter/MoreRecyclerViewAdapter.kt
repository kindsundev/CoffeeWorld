package kind.sun.dev.coffeeworld.ui.more.adapter

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
import kind.sun.dev.coffeeworld.utils.data.MoreOptionUtils

class MoreRecyclerViewAdapter : RecyclerView.Adapter<MoreRecyclerViewHolder>() {

    private var doubleBoxItems = mutableListOf<MoreRecyclerViewItem.Item>()

    var onItemClickListener: ((itemId: MoreOptionUtils.Id) -> Unit)? = null

    var items = listOf<MoreRecyclerViewItem>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreRecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            R.layout.item_more_title -> {
                MoreRecyclerViewHolder.TitleViewHolder(
                    ItemMoreTitleBinding.inflate(layoutInflater, parent, false)
                )
            }
            R.layout.item_more_box_only -> {
                MoreRecyclerViewHolder.OnlyBoxViewHolder(
                    ItemMoreBoxOnlyBinding.inflate(layoutInflater, parent, false)
                )
            }
            R.layout.item_more_box_double -> {
                MoreRecyclerViewHolder.DoubleBoxViewHolder(
                    ItemMoreBoxDoubleBinding.inflate(layoutInflater, parent, false)
                )
            }
            R.layout.item_more_row -> {
                MoreRecyclerViewHolder.RowViewHolder(
                    ItemMoreRowBinding.inflate(layoutInflater, parent, false)
                )
            }
            else -> {
                MoreRecyclerViewHolder.TempViewHolder(
                    ItemMoreTempBinding.inflate(layoutInflater, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: MoreRecyclerViewHolder, position: Int) {
        val item = items[position].also {
            if (it is MoreRecyclerViewItem.Item && it.type == MoreRecyclerViewItem.BOX_DOUBLE_TYPE) {
                doubleBoxItems.add(it)
            }
            if (doubleBoxItems.size > 2) doubleBoxItems.clear()
        }

        holder.onItemClickListener = onItemClickListener

        when (holder) {
            is MoreRecyclerViewHolder.TitleViewHolder -> {
                holder.onBind(item as MoreRecyclerViewItem.Title)
            }
            is MoreRecyclerViewHolder.OnlyBoxViewHolder -> {
                holder.onBind(item as MoreRecyclerViewItem.Item)
            }
            is MoreRecyclerViewHolder.DoubleBoxViewHolder -> {
                if (doubleBoxItems.size == 2) {
                    holder.onBind(doubleBoxItems[0], doubleBoxItems[1])
                }
            }
            is MoreRecyclerViewHolder.RowViewHolder -> {
                holder.onBind(item as MoreRecyclerViewItem.Item)
            }
            else -> {}
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when(val item = items[position]) {
            is MoreRecyclerViewItem.Title -> R.layout.item_more_title
            is MoreRecyclerViewItem.Temp -> R.layout.item_more_box_double
            is MoreRecyclerViewItem.Item -> {
                when (item.type) {
                    MoreRecyclerViewItem.ROW_TYPE -> R.layout.item_more_row
                    MoreRecyclerViewItem.BOX_ONLY_TYPE -> R.layout.item_more_box_only
                    else -> { R.layout.item_more_temp }
                }
            }
        }
    }


}