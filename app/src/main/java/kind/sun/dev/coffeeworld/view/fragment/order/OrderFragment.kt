package kind.sun.dev.coffeeworld.view.fragment.order

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseFragment
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.data.local.model.CategoryModel
import kind.sun.dev.coffeeworld.data.local.model.DrinkModel
import kind.sun.dev.coffeeworld.data.local.model.MenuModel
import kind.sun.dev.coffeeworld.databinding.FragmentOrderBinding
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.common.Logger
import kind.sun.dev.coffeeworld.utils.dataset.OrderDataSet
import kind.sun.dev.coffeeworld.utils.helper.view.isExist
import kind.sun.dev.coffeeworld.utils.helper.view.showToastError
import kind.sun.dev.coffeeworld.utils.helper.view.smoothToPositionWithOffset
import kind.sun.dev.coffeeworld.view.adapter.order.menu.OrderMenuAdapter
import kind.sun.dev.coffeeworld.view.adapter.order.menu.OrderMenuViewItem
import kind.sun.dev.coffeeworld.view.bsdf.order.OrderCafeShopBSDF
import kind.sun.dev.coffeeworld.view.bsdf.order.OrderCategoryBSDF
import kind.sun.dev.coffeeworld.viewmodel.CafeViewModel
import kind.sun.dev.coffeeworld.viewmodel.OrderViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class OrderFragment : BaseFragment<FragmentOrderBinding, OrderViewModel>(
    FragmentOrderBinding::inflate
){
    override val viewModel: OrderViewModel by viewModels()
    private val cafeViewModel: CafeViewModel by viewModels()

    private var scope = CoroutineScope(Dispatchers.IO)
    private lateinit var menuAdapter: OrderMenuAdapter
    private var cafes: List<CafeModel>? = null
    private var categories: List<CategoryModel>? = null


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
            cafes = checkCafeListFromLocal()
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
        scope.launch {
            val data = cafeViewModel.convertToMenuViewItem(result.beverageCategories).also {
                if (it[0] is OrderMenuViewItem.Categories) {
                    categories = (it[0] as OrderMenuViewItem.Categories).categories
                    Logger.error(message = "${categories?.size}")
                }
            }
            withContext(Dispatchers.Main) {
                menuAdapter = OrderMenuAdapter(data)
                binding.rvMenu.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = menuAdapter
                }
                menuAdapter.onItemClickListener = { type, id, drink ->
                    when(type) {
                        Constants.ORDER_BANNER_EVENT -> onBannerClickListener(id)
                        Constants.ORDER_CATEGORY_EVENT -> onCategoryClickListener(id)
                        Constants.ORDER_COFFEE_ROOT_EVENT -> drink?.let { onDrinkRootClickListener(it) }
                        Constants.ORDER_COFFEE_FAB_EVENT -> drink?.let { onDrinkFabCLickListener(it) }
                    }
                }
            }
        }
    }

    private fun onBannerClickListener(id: Int) {
        // todo: navigate to fragment (init coffees of type)
        when(id) {
            OrderDataSet.SPRING_BANNER -> {

            }
            OrderDataSet.SUMMER_BANNER -> {

            }
            OrderDataSet.FALL_BANNER -> {

            }
            OrderDataSet.WINTER_BANNER -> {

            }
            OrderDataSet.INTERNAL_DAY_BANNER -> {

            }
        }
    }

    private fun onCategoryClickListener(id: Int) {
        if (id == Constants.CATEGORY_MORE_ID) {
            OrderCategoryBSDF.newInstance(childFragmentManager, categories) {
                onCategoryClickListener(it)
            }
            return
        }
        menuAdapter.getCoffeesPositionByCategoryId(id).let { position ->
            if (position != OrderMenuAdapter.NO_POSITION) {
                binding.rvMenu.smoothToPositionWithOffset(position)
            }
        }
    }

    private fun onDrinkRootClickListener(drink: DrinkModel) {
        // todo navigate x.x.x fragment
    }

    private fun onDrinkFabCLickListener(drink: DrinkModel) {
        // todo navigate x.x.x bottom sheet fragment
    }

    override fun observeViewModel() {
        observeCafeResult()
    }

    private fun observeCafeResult() = cafeViewModel.apply {
        cafe.observeNetworkResult(
            onSuccess = {
                cafes = it.data
                cafes?.let {
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
        OrderCafeShopBSDF.newInstance(childFragmentManager, cafes) {
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

    override fun cleanUp() {
        super.cleanUp()
        scope.cancel()
    }
}