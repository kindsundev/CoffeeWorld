package kind.sun.dev.coffeeworld.view.fragment.cafe

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseBottomSheet
import kind.sun.dev.coffeeworld.base.BaseViewModel
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.databinding.FragmentCafeShopDetailBinding
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.helper.view.getParcelableSafe

@AndroidEntryPoint
class CafeShopDetailFragment : BaseBottomSheet<FragmentCafeShopDetailBinding, BaseViewModel>(
    isFullScreen = true, FragmentCafeShopDetailBinding::inflate
){
    private var cafeModel: CafeModel? = null

    override val viewModel: BaseViewModel  by viewModels()

    override fun setupDataBinding() {
        binding.fragment = this
    }

    override fun prepareData() {
        cafeModel = arguments?.getParcelableSafe(Constants.CAFE_KEY)
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