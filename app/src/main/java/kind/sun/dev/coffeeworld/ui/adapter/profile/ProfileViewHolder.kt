package kind.sun.dev.coffeeworld.ui.adapter.profile

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.data.local.model.UserModel
import kind.sun.dev.coffeeworld.databinding.ItemMoreRowBinding
import kind.sun.dev.coffeeworld.databinding.ItemMoreTitleBinding
import kind.sun.dev.coffeeworld.databinding.ItemUserInfoBinding
import kind.sun.dev.coffeeworld.ui.adapter.more.MoreViewItem
import kind.sun.dev.coffeeworld.util.dataset.MoreDataSet
import kind.sun.dev.coffeeworld.util.helper.view.setOnClickScaleListener

sealed class ProfileViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    var onItemClickListener : ((itemId: (MoreDataSet.Id)) -> Unit)? = null

    class TitleViewHolder(private val binding: ItemMoreTitleBinding): ProfileViewHolder(binding) {
        fun bindView(title: MoreViewItem.Title) = binding.tvTitle.setText(title.resTitle)
    }

    class UserInfoViewHolder(private val binding: ItemUserInfoBinding): ProfileViewHolder(binding) {

        fun bindView(user: UserModel) = binding.apply {
            userModel = user
            imgAvatar.setOnClickScaleListener { onItemClickListener?.invoke(MoreDataSet.Id.AVATAR) }
            root.setOnClickScaleListener { onItemClickListener?.invoke(MoreDataSet.Id.PROFILE) }
            executePendingBindings()
        }
    }

    class OptionViewHolder(private val binding: ItemMoreRowBinding): ProfileViewHolder(binding) {

        fun bindView(data: MoreViewItem.Item) = binding.apply {
            item = data
            root.setOnClickScaleListener { onItemClickListener?.invoke(data.id) }
            executePendingBindings()
            applyNewStyle(data)
        }


        private fun applyNewStyle(data: MoreViewItem.Item) {
            binding.apply {
                when(data.id) {
                    MoreDataSet.Id.NAME -> {
                        root.setBackgroundResource(R.drawable.bg_layout_solid_white_border_top_8)
                        imgIconLogo.apply {
                            setLogoColor(context, data.resLogo, R.color.purple_dark)?.let {
                                setImageDrawable(it)
                            }
                        }
                    }
                    MoreDataSet.Id.EMAIL -> {
                        imgIconLogo.apply {
                            setLogoColor(context, data.resLogo, R.color.orange_dark)?.let {
                                setImageDrawable(it)
                            }
                        }
                    }
                    MoreDataSet.Id.PASSWORD -> {
                        imgIconLogo.apply {
                            setLogoColor(context, data.resLogo, R.color.gray)?.let {
                                setImageDrawable(it)
                            }
                        }
                    }
                    MoreDataSet.Id.ADDRESS -> {
                        imgIconLogo.apply {
                            setLogoColor(context, data.resLogo, R.color.blue)?.let {
                                setImageDrawable(it)
                            }
                        }
                    }
                    MoreDataSet.Id.PHONE -> {
                        root.setBackgroundResource(R.drawable.bg_layout_solid_white_border_bottom_8)
                        viewDivider.visibility = View.GONE
                        imgIconLogo.apply {
                            setLogoColor(context, data.resLogo, R.color.green_dark)?.let {
                                setImageDrawable(it)
                            }
                        }
                    }
                    MoreDataSet.Id.SWITCH_ACCOUNT -> {
                        root.setBackgroundResource(R.drawable.bg_layout_solid_white_border_top_8)
                    }
                    MoreDataSet.Id.LOG_OUT -> {
                        root.setBackgroundResource(R.drawable.bg_layout_solid_white_border_bottom_8)
                    }
                    else -> {}
                }
            }
        }

       /*
       * When initializing model item.
       * The drawable resources are reused, but in my design the colors will be different.
       * So instead of creating a corresponding resource with just a different color,
       * I chose to copy it and change the color to optimize.
       * */
        private fun setLogoColor(context: Context, resLogo: Int, resColor: Int): Drawable? {
            return ContextCompat.getDrawable(context, resLogo)?.let {
                it.mutate().apply {
                    DrawableCompat.setTint(it, ContextCompat.getColor(context, resColor))
                }
            }
        }
    }
}