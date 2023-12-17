package kind.sun.dev.coffeeworld.view.fragment.order

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kind.sun.dev.coffeeworld.base.BaseBottomSheet
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.databinding.FragmentOrderCafeShopBinding
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.helper.view.getParcelableArrayListHelper
import kind.sun.dev.coffeeworld.utils.helper.view.remove
import kind.sun.dev.coffeeworld.utils.helper.view.show
import kind.sun.dev.coffeeworld.view.adapter.order.shop.OrderCafeShopAdapter
import kind.sun.dev.coffeeworld.viewmodel.CafeViewModel
import java.util.ArrayList

class OrderCafeShopFragment : BaseBottomSheet<FragmentOrderCafeShopBinding, CafeViewModel>(
    isFullScreen = false, bindingInflater = FragmentOrderCafeShopBinding::inflate
){
    override val viewModel: CafeViewModel by viewModels()
    private var cafeModels: List<CafeModel>?= null
    private var onItemClicked: ((id: Int) -> Unit)? = null

    companion object {
        fun newInstance(
            fragmentManager: FragmentManager,
            data: List<CafeModel>?,
            onItemClickListener: ((id: Int) -> Unit)? = null
        ) {
            return OrderCafeShopFragment().apply {
                data?.let {
                    arguments = Bundle().apply {
                        putParcelableArrayList(
                            Constants.ORDER_CAFE_KEY, it as ArrayList<out Parcelable>
                        )
                    }
                    onItemClicked = onItemClickListener
                }
            }.show(fragmentManager, OrderCafeShopFragment::class.simpleName)
        }
    }

    override fun prepareData() {
        cafeModels = arguments?.getParcelableArrayListHelper(Constants.ORDER_CAFE_KEY)
    }

    override fun setupDataBinding() {}

    override fun initViews() {
        binding.apply {
            if (cafeModels.isNullOrEmpty()) {
                toggleRecyclerView(false)
            } else {
                toggleRecyclerView(true)
                rvCafeShop.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = OrderCafeShopAdapter(cafeModels!!) {
                        onItemClicked?.invoke(it)
                        this@OrderCafeShopFragment.dismiss()
                    }
                    hasFixedSize()
                }
            }
        }
    }

    private fun toggleRecyclerView(isShow: Boolean) = binding.apply {
        if (isShow && rvCafeShop.visibility == View.GONE) {
            emptyShop.remove()
            rvCafeShop.show()
        }
        if (!isShow && rvCafeShop.visibility == View.VISIBLE) {
            rvCafeShop.remove()
            emptyShop.show()
        }
    }

    override fun observeViewModel() {}
}