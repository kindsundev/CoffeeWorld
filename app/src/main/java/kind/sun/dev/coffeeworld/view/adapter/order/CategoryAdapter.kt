package kind.sun.dev.coffeeworld.view.adapter.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kind.sun.dev.coffeeworld.data.local.model.CategoryModel
import kind.sun.dev.coffeeworld.databinding.ItemCategoryCircleBinding
import kind.sun.dev.coffeeworld.databinding.ItemCategoryMoreBinding
import kind.sun.dev.coffeeworld.utils.common.Constants

class CategoryAdapter(
    private val categories: List<CategoryModel>,
    private val onCategoryClick: (CategoryModel) -> Unit,
    private val onMoreClick: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_REAL_ITEM = 1
        private const val VIEW_TYPE_MORE_ITEM = 2
    }

    inner class ItemCategoryViewHolder(private val binding: ItemCategoryCircleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryModel) {
            binding.category = category
            binding.root.setOnClickListener { onCategoryClick(category) }
            binding.executePendingBindings()
        }
    }

    inner class ItemMoreViewHolder(private val binding: ItemCategoryMoreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.tvMore.text = Constants.MORE
            binding.root.setOnClickListener {onMoreClick.invoke() }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_MORE_ITEM) {
            ItemMoreViewHolder(ItemCategoryMoreBinding.inflate(inflater, parent, false))
        } else {
            ItemCategoryViewHolder(ItemCategoryCircleBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemCategoryViewHolder) {
            holder.bind(categories[position])
        } else if (holder is ItemMoreViewHolder) {
            holder.bind()
        }
    }

    override fun getItemCount(): Int {
        return if (categories.size >= 8) 8 else categories.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 7) VIEW_TYPE_MORE_ITEM else VIEW_TYPE_REAL_ITEM
    }

}