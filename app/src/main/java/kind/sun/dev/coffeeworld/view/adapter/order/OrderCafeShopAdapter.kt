package kind.sun.dev.coffeeworld.view.adapter.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.databinding.ItemCafeShopRowBinding

class OrderCafeShopAdapter(
    private val data: List<CafeModel>,
    private val onItemClickListener: (id: Int) -> Unit
): RecyclerView.Adapter<OrderCafeShopAdapter.CafeViewHolder>() {

    inner class CafeViewHolder(
        private val binding: ItemCafeShopRowBinding
    ): RecyclerView.ViewHolder(binding.root) {

        internal fun bindView(cafeModel: CafeModel) = binding.apply {
            cafe = cafeModel
            root.setOnClickListener {
                onItemClickListener.invoke(cafeModel.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeViewHolder {
        return CafeViewHolder(
            ItemCafeShopRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: CafeViewHolder, position: Int) {
        holder.bindView(data[position])
    }
}