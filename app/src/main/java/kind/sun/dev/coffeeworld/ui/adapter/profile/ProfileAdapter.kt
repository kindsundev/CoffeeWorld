package kind.sun.dev.coffeeworld.ui.adapter.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kind.sun.dev.coffeeworld.data.local.model.UserModel
import kind.sun.dev.coffeeworld.databinding.ItemMoreRowBinding
import kind.sun.dev.coffeeworld.databinding.ItemMoreTitleBinding
import kind.sun.dev.coffeeworld.databinding.ItemUserInfoBinding
import kind.sun.dev.coffeeworld.ui.adapter.more.MoreViewItem
import kind.sun.dev.coffeeworld.util.dataset.MoreDataSet
import java.lang.IllegalArgumentException

class ProfileAdapter(
    private val items: List<MoreViewItem>,
    private var onItemCLickListener: ((itemId: MoreDataSet.Id) -> Unit)? = null
) : RecyclerView.Adapter<ProfileViewHolder>() {

    var user: UserModel? = null
        set(value) {
            field = value
            notifyItemChanged(0)
        }

    companion object {
        const val USER_TYPE = 1
        const val TITLE_TYPE = 2
        const val ITEM_TYPE = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            USER_TYPE -> {
                ProfileViewHolder.UserInfoViewHolder(
                    ItemUserInfoBinding.inflate(layoutInflater, parent, false)
                )
            }
            TITLE_TYPE -> {
                ProfileViewHolder.TitleViewHolder(
                    ItemMoreTitleBinding.inflate(layoutInflater, parent, false)
                )
            }
            ITEM_TYPE -> {
                ProfileViewHolder.OptionViewHolder(
                    ItemMoreRowBinding.inflate(layoutInflater, parent, false)
                )
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val item = items[position]
        holder.onItemClickListener = onItemCLickListener
        when(holder) {
            is ProfileViewHolder.UserInfoViewHolder -> user?.let { holder.bindView(it) }
            is ProfileViewHolder.TitleViewHolder -> holder.bindView(item as MoreViewItem.Title)
            is ProfileViewHolder.OptionViewHolder -> holder.bindView(item as MoreViewItem.Item)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when(val item = items[position]) {
            is MoreViewItem.Title -> TITLE_TYPE
            is MoreViewItem.Item -> item.type
            else -> -1
        }
    }

}