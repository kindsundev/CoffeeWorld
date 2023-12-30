package kind.sun.dev.coffeeworld.ui.bsdf.order

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.base.BaseBSDF
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.databinding.BsdfSelectItemDefaultBinding
import kind.sun.dev.coffeeworld.util.common.Constants
import kind.sun.dev.coffeeworld.util.helper.view.getParcelableArrayListHelper
import kind.sun.dev.coffeeworld.util.helper.view.remove
import kind.sun.dev.coffeeworld.util.helper.view.show
import kind.sun.dev.coffeeworld.ui.adapter.order.shop.OrderCafeShopAdapter
import kind.sun.dev.coffeeworld.viewmodel.CafeViewModel
import java.util.ArrayList

@AndroidEntryPoint
class OrderCafeShopBSDF : BaseBSDF<BsdfSelectItemDefaultBinding, CafeViewModel>(
    layoutInflater = BsdfSelectItemDefaultBinding::inflate,
    viewModelClass = CafeViewModel::class.java,
    isFullScreen = false,
){
    private var cafes: List<CafeModel>?= null
    var onItemClicked: ((id: Int) -> Unit)? = null

    companion object {
        fun newInstance(
            fragmentManager: FragmentManager,
            data: List<CafeModel>?,
            onItemClickListener: ((id: Int) -> Unit)? = null
        ) {
            return OrderCafeShopBSDF().apply {
                data?.let {
                    arguments = Bundle().apply {
                        putParcelableArrayList(
                            Constants.ORDER_CAFE_KEY, it as ArrayList<out Parcelable>
                        )
                    }
                    onItemClicked = onItemClickListener
                }
            }.show(fragmentManager, OrderCafeShopBSDF::class.simpleName)
        }
    }

    override fun prepareData() {
        cafes = arguments?.getParcelableArrayListHelper(Constants.ORDER_CAFE_KEY)
    }

    override fun initViews() {
        binding.apply {
            tvTitle.text = requireContext().getString(R.string.select_cafe_shop)
            if (cafes.isNullOrEmpty()) {
                toggleRecyclerView(false)
            } else {
                toggleRecyclerView(true)
                rvItems.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = OrderCafeShopAdapter(cafes!!) {
                        onItemClicked?.invoke(it)
                        this@OrderCafeShopBSDF.dismiss()
                    }
                    hasFixedSize()
                }
            }
        }
    }

    private fun toggleRecyclerView(isShow: Boolean) = binding.apply {
        if (isShow && rvItems.visibility == View.GONE) {
            emptyShop.remove()
            rvItems.show()
        }
        if (!isShow && rvItems.visibility == View.VISIBLE) {
            rvItems.remove()
            emptyShop.show()
        }
    }
}