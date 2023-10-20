package kind.sun.dev.coffeeworld.view.adapter.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.data.local.model.UserModel
import kind.sun.dev.coffeeworld.databinding.ItemMoreRowBinding
import kind.sun.dev.coffeeworld.databinding.ItemMoreTitleBinding
import kind.sun.dev.coffeeworld.databinding.ItemUserInfoBinding
import kind.sun.dev.coffeeworld.view.adapter.more.MoreViewItem
import kind.sun.dev.coffeeworld.utils.dataset.MoreDataSet
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            R.layout.item_user_info -> {
                ProfileViewHolder.UserInfoViewHolder(
                    ItemUserInfoBinding.inflate(layoutInflater, parent, false)
                )
            }
            R.layout.item_more_title -> {
                ProfileViewHolder.TitleViewHolder(
                    ItemMoreTitleBinding.inflate(layoutInflater, parent, false)
                )
            }
            R.layout.item_more_row -> {
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
            is ProfileViewHolder.UserInfoViewHolder -> user?.let { holder.onBind(it) }
            is ProfileViewHolder.TitleViewHolder -> holder.onBind(item as MoreViewItem.Title)
            is ProfileViewHolder.OptionViewHolder -> holder.onBind(item as MoreViewItem.Item)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when(items[position]) {
            is MoreViewItem.Temp -> R.layout.item_user_info
            is MoreViewItem.Title -> R.layout.item_more_title
            is MoreViewItem.Item -> R.layout.item_more_row
        }
    }

}