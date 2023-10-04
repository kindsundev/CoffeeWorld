package kind.sun.dev.coffeeworld.view.adapter.cafe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.data.model.response.cafe.CafeModel
import kind.sun.dev.coffeeworld.databinding.ItemCafeBinding
import kind.sun.dev.coffeeworld.utils.common.Constants

class CafeAdapter(
    private val onCafeClick: (CafeModel) -> Unit
) : ListAdapter<CafeModel, CafeAdapter.CafeViewHolder>(CafeDiffCallback()) {

    inner class CafeViewHolder(private val binding: ItemCafeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cafe: CafeModel) {
            binding.cafe = cafe
            binding.tvDistance.text = Constants.randomLocations[cafe.id]
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                onCafeClick.invoke(cafe)
            }
        }
    }

    private class CafeDiffCallback : DiffUtil.ItemCallback<CafeModel>() {
        override fun areItemsTheSame(oldItem: CafeModel, newItem: CafeModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CafeModel, newItem: CafeModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemCafeBinding>(
            layoutInflater, R.layout.item_cafe, parent, false
        )
        return CafeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CafeViewHolder, position: Int) {
        val cafe: CafeModel = getItem(position)
        holder.bind(cafe)
    }
}