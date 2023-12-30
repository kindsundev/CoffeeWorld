package kind.sun.dev.coffeeworld.ui.adapter.order.shop

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.databinding.ItemCafeShopRowBinding
import kind.sun.dev.coffeeworld.util.helper.view.inflateBinding

class OrderCafeShopAdapter(
    private val data: List<CafeModel>,
    private val onItemClickListener: (id: Int) -> Unit
): RecyclerView.Adapter<OrderCafeShopAdapter.CafeViewHolder>() {

    inner class CafeViewHolder(
        private val binding: ItemCafeShopRowBinding
    ): RecyclerView.ViewHolder(binding.root) {

        internal fun onBind(cafeModel: CafeModel) = binding.apply {
            cafe = cafeModel
            root.setOnClickListener {
                onItemClickListener.invoke(cafeModel.id)
            }
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeViewHolder {
        return CafeViewHolder(parent.inflateBinding(ItemCafeShopRowBinding::inflate))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: CafeViewHolder, position: Int) {
        holder.onBind(data[position])
    }
}