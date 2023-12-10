package kind.sun.dev.coffeeworld.view.fragment.cafe

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseBottomSheet
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.databinding.FragmentCafeShopDetailBinding
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.common.Logger
import kind.sun.dev.coffeeworld.utils.helper.view.getParcelableHelper
import kind.sun.dev.coffeeworld.utils.helper.view.isExist
import kind.sun.dev.coffeeworld.viewmodel.CafeViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CafeShopDetailFragment : BaseBottomSheet<FragmentCafeShopDetailBinding, CafeViewModel>(
    isFullScreen = true, FragmentCafeShopDetailBinding::inflate
){
    private var cafeModel: CafeModel? = null

    override val viewModel: CafeViewModel  by viewModels()


    override fun setupDataBinding() {
        binding.fragment = this
        binding.lifecycleOwner = this
    }

    override fun prepareData() {
        cafeModel = arguments?.getParcelableHelper(Constants.CAFE_KEY)
        lifecycleScope.launch {
            cafeModel?.id?.let { id ->
                viewModel.onFetchMenu( false, id, {}, {})
            }
        }

    }

    override fun initViews() {
        cafeModel?.let {
            binding.apply {
                cafeModel = it
                tvLocationOverview.text = it.location.split(",")[0]
            }
        }
    }

    override fun observeViewModel() {
        viewModel.menu.observeNetworkResult(
            onSuccess = {
                lifecycleScope.launch {
                    if (it.success) {
                        viewModel.onSyncMenu(it.data)
                    }
                }
            }, onError = {}
        )
    }

    fun onExit(): Unit = exit()
}