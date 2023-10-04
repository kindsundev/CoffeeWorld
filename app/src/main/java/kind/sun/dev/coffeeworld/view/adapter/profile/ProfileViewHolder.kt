package kind.sun.dev.coffeeworld.view.adapter.profile

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.data.model.response.auth.UserModel
import kind.sun.dev.coffeeworld.databinding.ItemMoreRowBinding
import kind.sun.dev.coffeeworld.databinding.ItemMoreTitleBinding
import kind.sun.dev.coffeeworld.databinding.ItemUserInfoBinding
import kind.sun.dev.coffeeworld.view.adapter.more.MoreViewItem
import kind.sun.dev.coffeeworld.utils.helper.animation.setScaleAnimation
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.dataset.MoreDataSet

sealed class ProfileViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    var onItemClickListener : ((itemId: (MoreDataSet.Id)) -> Unit)? = null

    protected fun onClickMoreItem(view: View, optionId: MoreDataSet.Id) {
        view.setScaleAnimation(Constants.DURATION_SHORT, Constants.SCALE_LOW) {
            onItemClickListener?.invoke(optionId)
        }
    }

    class TitleViewHolder(private val binding: ItemMoreTitleBinding): ProfileViewHolder(binding) {
        fun onBind(title: MoreViewItem.Title) {
            binding.tvTitle.setText(title.resTitle)
        }
    }

    class UserInfoViewHolder(private val binding: ItemUserInfoBinding): ProfileViewHolder(binding) {
        fun onBind(user: UserModel) {
            binding.apply {
                userModel = user
                executePendingBindings()
                imgAvatar.setOnClickListener { onClickMoreItem(it, MoreDataSet.Id.AVATAR) }
                root.setOnClickListener { onClickMoreItem(it, MoreDataSet.Id.PROFILE) }
            }
        }
    }

    class OptionViewHolder(private val binding: ItemMoreRowBinding): ProfileViewHolder(binding) {
        fun onBind(data: MoreViewItem.Item) {
            binding.apply {
                item = data
                executePendingBindings()
                root.setOnClickListener { onClickMoreItem(it, data.id) }
                applyNewStyle(data)
            }
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