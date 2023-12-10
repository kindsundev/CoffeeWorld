package kind.sun.dev.coffeeworld.view.fragment.order

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseFragment
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.data.local.model.MenuModel
import kind.sun.dev.coffeeworld.databinding.FragmentOrderBinding
import kind.sun.dev.coffeeworld.utils.common.Logger
import kind.sun.dev.coffeeworld.utils.helper.view.isExist
import kind.sun.dev.coffeeworld.utils.helper.view.showToastError
import kind.sun.dev.coffeeworld.viewmodel.CafeViewModel
import kind.sun.dev.coffeeworld.viewmodel.OrderViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderFragment : BaseFragment<FragmentOrderBinding, OrderViewModel>(
    FragmentOrderBinding::inflate
) {
    override val viewModel: OrderViewModel by viewModels()
    private val cafeViewModel: CafeViewModel by viewModels()

    private var cafeList: List<CafeModel>?= null
    private var menuModel: MenuModel? = null

    private var hasCafeId: Boolean = false
        get() = preferences.currentCafeId?.isExist() ?: false


    override fun initAnything() {
        requireActivity().onBackPressedDispatcher.addCallback(
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    popToHomeFragment()
                }
            }
        )
    }

    override fun setupDataBinding() {
        binding.fragment = this
        binding.lifecycleOwner = this
    }

    override fun prepareData() {
        lifecycleScope.launch {
            cafeList = checkCafeListFromLocal()
            menuModel = checkMenuModelFromLocal()
        }
    }

    private suspend fun checkCafeListFromLocal() : List<CafeModel>? {
        cafeViewModel.apply {
            onRetrieveAllCafes()?.let result@{
                return@result
            } ?: onFetchAllCafes(true, {},{})
        }
        return null
    }

    private suspend fun checkMenuModelFromLocal(): MenuModel? {
        cafeViewModel.apply {
            if (hasCafeId) onRetrieveMenu(preferences.currentCafeId!!)?.let result@{
                return@result
            } ?: onFetchMenu(true, preferences.currentCafeId!!,{}, {})
        }
        return null
    }

    override fun initViews() {
        // todo: check data (current has problem!)
        if (menuModel != null) {
            initMenuLayout()
        }
    }

    override fun observeViewModel() { observeCafeResult() }

    private fun observeCafeResult() = cafeViewModel.apply {
        cafe.observeNetworkResult(
            onSuccess = {
                cafeList = it.data
                if (!hasCafeId) requestPickCafeShop()
            },
            onError = { requireContext().showToastError(it) }
        )

        menu.observeNetworkResult(
            onSuccess = { menuModel = it.data },
            onError = { requireContext().showToastError(it) }
        )
    }

    fun onClickCafeShop() = requestPickCafeShop()

    private fun requestPickCafeShop(){
        OrderCafeShopFragment.newInstance(childFragmentManager, cafeList) {
            preferences.currentCafeId = it.also {
                lifecycleScope.launch {
                    cafeViewModel.onFetchMenu(true, it, {}, {})
                }
            }
        }
    }

    private fun initMenuLayout() = binding.apply {
        menuModel?.let { Logger.debug("${it.beverageCategories.size}") } ?: Logger.warning("null")
    }

    fun onClickSearch() {

    }

    fun onClickFavorite() {

    }
}