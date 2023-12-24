package kind.sun.dev.coffeeworld.view.bsdf.cafe

import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseBSDF
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.databinding.BsdfCafeShopDetailBinding
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.helper.view.getParcelableHelper
import kind.sun.dev.coffeeworld.viewmodel.CafeViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CafeShopDetailBSDF : BaseBSDF<BsdfCafeShopDetailBinding, CafeViewModel>(
    layoutInflater = BsdfCafeShopDetailBinding::inflate,
    viewModelClass = CafeViewModel::class.java,
    isFullScreen = true,
){
    private var cafeModel: CafeModel? = null

    override fun setupDataBinding() {
        binding.fragment = this
        binding.lifecycleOwner = this
    }

    override fun prepareData() {
        cafeModel = arguments?.getParcelableHelper(Constants.CAFE_KEY)
        lifecycleScope.launch {
            cafeModel?.id?.let { id ->
                viewModel?.onFetchMenu( true, id)
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
        viewModel!!.menu.observeNetworkResult(
            onSuccess = {
                lifecycleScope.launch {
                    if (it.success) {
                        viewModel!!.onSyncMenu(it.data)
                    }
                }
            }, onError = {}
        )
    }

    fun onExit(): Unit = exit()
}