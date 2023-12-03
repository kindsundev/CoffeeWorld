package kind.sun.dev.coffeeworld.view.fragment.cafe

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.base.BaseBottomSheet
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.databinding.FragmentCafeShopDetailBinding
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.common.Logger
import kind.sun.dev.coffeeworld.utils.helper.view.getParcelableSafe
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
    }

    override fun prepareData() {
        cafeModel = arguments?.getParcelableSafe(Constants.CAFE_KEY)
        /*
        * todo:
        *  - get menu but not show progress (because: prepare and ux)
        *  - update MenuModel has property cafeId: Int (backend and this)
        * */
        lifecycleScope.launch {
            cafeModel?.id?.let {
                viewModel.onFetchMenu(it,
                    onDataFromLocal = {

                    },
                    onFailedMessage = {

                    }
                )
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
                Logger.error("Result: ${it.data.cafeId} - ${it.data.beverageCategories.size}")
            },
            onError = {
                StyleableToast.makeText(requireContext(), it, R.style.toast_error).show()
            })
    }

    fun onExit(): Unit = exit()
}