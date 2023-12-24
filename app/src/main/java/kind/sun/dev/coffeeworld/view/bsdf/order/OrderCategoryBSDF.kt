package kind.sun.dev.coffeeworld.view.bsdf.order

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.base.BaseBSDF
import kind.sun.dev.coffeeworld.data.local.model.CategoryModel
import kind.sun.dev.coffeeworld.databinding.BsdfSelectItemDefaultBinding
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.helper.view.getParcelableArrayListHelper
import kind.sun.dev.coffeeworld.utils.helper.view.remove
import kind.sun.dev.coffeeworld.utils.helper.view.show
import kind.sun.dev.coffeeworld.view.adapter.order.category.OrderCategoryAdapter
import kind.sun.dev.coffeeworld.viewmodel.CafeViewModel

class OrderCategoryBSDF : BaseBSDF<BsdfSelectItemDefaultBinding, CafeViewModel>(
    layoutInflater = BsdfSelectItemDefaultBinding::inflate,
    viewModelClass = CafeViewModel::class.java,
    isFullScreen = false,
) {
    private var categories: List<CategoryModel>? = null
    var onItemClicked: ((id: Int) -> Unit)? = null

    companion object {
        fun newInstance(
            fragmentManager: FragmentManager,
            data: List<CategoryModel>?,
            onItemClickListener: ((id: Int) -> Unit)? = null
        ) {
            return OrderCategoryBSDF().apply {
                data?.let {
                    arguments = Bundle().apply {
                        putParcelableArrayList(Constants.ORDER_CATEGORY_KEY, it as ArrayList<out Parcelable>)
                    }
                    onItemClicked = onItemClickListener
                }

            }.show(fragmentManager, OrderCafeShopBSDF::class.simpleName)
        }
    }

    override fun prepareData() {
        categories = arguments?.getParcelableArrayListHelper(Constants.ORDER_CATEGORY_KEY)
    }

    override fun setupDataBinding() {}

    override fun initViews() {
        binding.tvTitle.text = requireContext().getString(R.string.select_category)
        if (categories.isNullOrEmpty()) {
            toggleRecyclerView(false)
        } else {
            toggleRecyclerView(true)
            binding.rvItems.apply {
                layoutManager = GridLayoutManager(requireContext(), 4)
                adapter = OrderCategoryAdapter(categories!!) {
                    onItemClicked?.invoke(it)
                    this@OrderCategoryBSDF.dismiss()
                }

                hasFixedSize()
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