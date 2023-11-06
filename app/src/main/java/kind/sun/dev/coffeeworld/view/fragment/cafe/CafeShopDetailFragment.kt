package kind.sun.dev.coffeeworld.view.fragment.cafe

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseBottomSheet
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.databinding.FragmentCafeShopDetailBinding
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.helper.view.getParcelableSafe
import kind.sun.dev.coffeeworld.viewmodel.CafeViewModel

@AndroidEntryPoint
class CafeShopDetailFragment : BaseBottomSheet<FragmentCafeShopDetailBinding, CafeViewModel>(
    isFullScreen = true, FragmentCafeShopDetailBinding::inflate
){
    private var cafeModel: CafeModel? = null

    override val viewModel: CafeViewModel  by viewModels()

    override fun setupDataBinding() {
        binding.fragment = this
    }

    override fun prepareData() {
        cafeModel = arguments?.getParcelableSafe(Constants.CAFE_KEY)
        /*
        * todo:
        *  - get menu but not show progress (because: prepare and ux)
        *  - update MenuModel has property cafeId: Int (backend and this)
        * */

    }

    override fun initViews() {
        cafeModel?.let {
            binding.apply {
                cafeModel = it
                tvLocationOverview.text = it.location.split(",")[0]
            }
        }
    }

    override fun observeViewModel() {}

    fun onExit(): Unit = exit()
}