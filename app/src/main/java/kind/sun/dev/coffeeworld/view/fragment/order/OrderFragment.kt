package kind.sun.dev.coffeeworld.view.fragment.order

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseFragment
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.databinding.FragmentOrderBinding
import kind.sun.dev.coffeeworld.utils.common.Logger
import kind.sun.dev.coffeeworld.utils.helper.view.showToast
import kind.sun.dev.coffeeworld.viewmodel.CafeViewModel
import kind.sun.dev.coffeeworld.viewmodel.OrderViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderFragment : BaseFragment<FragmentOrderBinding, OrderViewModel>(
    FragmentOrderBinding::inflate
) {
    override val viewModel: OrderViewModel by viewModels()
    private val cafeViewModel: CafeViewModel by viewModels()

    private var cafeModels: List<CafeModel>?= null

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
            cafeViewModel.onRetrieveAllCafes()?.let {
                cafeModels = it
//                binding.spinner.adapter = CafeSpinnerAdapter(requireContext(), it)
            } ?: cafeViewModel.onFetchAllCafes(
                isLoading = true, onDataFromLocal = {}, onFailedMessage = {}
            )
        }
    }

    override fun initViews() {

    }

    override fun observeViewModel() {

    }

    fun onClickCafeShop() {
        OrderCafeShopFragment.newInstance(childFragmentManager, cafeModels) {
            requireContext().showToast("$it")
        }
    }

    fun onClickSearch() {

    }

    fun onClickFavorite() {

    }
}