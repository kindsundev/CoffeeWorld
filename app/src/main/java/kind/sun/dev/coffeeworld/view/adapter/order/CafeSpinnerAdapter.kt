package kind.sun.dev.coffeeworld.view.adapter.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.data.model.response.cafe.CafeModel
import kind.sun.dev.coffeeworld.databinding.ItemCategorySpinnerBinding
import kind.sun.dev.coffeeworld.databinding.ItemSeletedSpinnerBinding

class CafeSpinnerAdapter(
    context: Context, resource: Int, objects: List<CafeModel>
): ArrayAdapter<CafeModel>(context, resource, objects){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ItemSeletedSpinnerBinding = if (convertView == null) {
            val layoutInflater = LayoutInflater.from(parent.context)
            DataBindingUtil.inflate(layoutInflater, R.layout.item_seleted_spinner, parent, false)
        } else {
            DataBindingUtil.getBinding(convertView) ?: return convertView
        }
        getItem(position)?.let { binding.cafe = it }
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ItemCategorySpinnerBinding = if (convertView == null) {
            val layoutInflater = LayoutInflater.from(parent.context)
            DataBindingUtil.inflate(layoutInflater, R.layout.item_category_spinner, parent, false)
        } else {
            DataBindingUtil.getBinding(convertView) ?: return convertView
        }
        getItem(position)?.let { binding.cafe = it }
        return binding.root
    }
}