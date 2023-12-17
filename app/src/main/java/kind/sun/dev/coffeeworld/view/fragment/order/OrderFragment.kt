package kind.sun.dev.coffeeworld.view.fragment.order

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseFragment
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.data.local.model.MenuModel
import kind.sun.dev.coffeeworld.databinding.FragmentOrderBinding
import kind.sun.dev.coffeeworld.utils.helper.view.isExist
import kind.sun.dev.coffeeworld.utils.helper.view.showToastError
import kind.sun.dev.coffeeworld.view.adapter.order.menu.OrderMenuAdapter
import kind.sun.dev.coffeeworld.viewmodel.CafeViewModel
import kind.sun.dev.coffeeworld.viewmodel.OrderViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderFragment : BaseFragment<FragmentOrderBinding, OrderViewModel>(
    FragmentOrderBinding::inflate
) {
    override val viewModel: OrderViewModel by viewModels()
    private val cafeViewModel: CafeViewModel by viewModels()

    private var cafeList: List<CafeModel>? = null
    private lateinit var menuAdapter: OrderMenuAdapter

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
            checkMenuModelFromLocal()
        }
    }

    private suspend fun checkCafeListFromLocal(): List<CafeModel>? {
        cafeViewModel.run {
            onRetrieveAllCafes()?.let { result ->
                return result
            } ?: onFetchAllCafes(true)
            return null
        }
    }

    private suspend fun checkMenuModelFromLocal(): MenuModel? {
        cafeViewModel.run {
            if (hasCafeId) {
                onRetrieveMenu(preferences.currentCafeId!!)?.let { result ->
                    onSyncMenu(result)
                    initMenuLayout(result)
                    return result
                } ?: onFetchMenu(true, preferences.currentCafeId!!)
            }
            return null
        }
    }

    override fun initViews() {
        // todo: init layout default, because result of request is call in the back
    }

    private fun initMenuLayout(result: MenuModel) {
        // todo: with context background thread here
        val data = cafeViewModel.convertToMenuViewItem(result.beverageCategories)
        // todo: with context main thread here
        menuAdapter = OrderMenuAdapter(data)
        binding.rvMenu.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = menuAdapter
        }
        /*
        * todo: event listener of aadapter:
        *  - click category => scroll to box drinks (category_id)
        *  - click banner => navigate to x.x.x.fragment
        *  - click coffee root => navigate to x.x.x.fragment
        *  - click coffee fab => navigate to x.x.x.fragment
        *  note: equals id at Constants
        * */
    }

    override fun observeViewModel() {
        observeCafeResult()
    }

    private fun observeCafeResult() = cafeViewModel.apply {
        cafe.observeNetworkResult(
            onSuccess = {
                cafeList = it.data
                cafeList?.let {
                    lifecycleScope.launch { onSyncAllCafes(it) }
                }
                if (!hasCafeId) requestPickCafeShop()
            },
            onError = { requireContext().showToastError(it) }
        )

        menu.observeNetworkResult(
            onSuccess = { result ->
                result?.data?.let {
                    lifecycleScope.launch { onSyncMenu(it) }
                    initMenuLayout(it)
                }
            },
            onError = {
                // todo: show ui empty
            }
        )
    }

    fun onClickCafeShop() = requestPickCafeShop()

    private fun requestPickCafeShop() {
        OrderCafeShopFragment.newInstance(childFragmentManager, cafeList) {
            preferences.currentCafeId = it.also {
                lifecycleScope.launch {
                    cafeViewModel.onFetchMenu(true, it)
                }
            }
        }
    }


    fun onClickSearch() {

    }

    fun onClickFavorite() {

    }
}