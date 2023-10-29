package kind.sun.dev.coffeeworld.view.fragment.cafe

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.base.BaseFragment
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.databinding.FragmentCafeBinding
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.helper.animation.setScaleAnimation
import kind.sun.dev.coffeeworld.utils.helper.view.checkThenHide
import kind.sun.dev.coffeeworld.utils.helper.view.showToast
import kind.sun.dev.coffeeworld.utils.helper.view.toggleNetworkErrorLayout
import kind.sun.dev.coffeeworld.view.adapter.cafe.CafeShopAdapter
import kind.sun.dev.coffeeworld.view.adapter.cafe.CafeShopViewItem
import kind.sun.dev.coffeeworld.viewmodel.CafeViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CafeFragment : BaseFragment<FragmentCafeBinding, CafeViewModel>(
    FragmentCafeBinding::inflate
){
    override val viewModel: CafeViewModel by viewModels()
    private lateinit var transformedData: List<CafeShopViewItem>

    private val cafeShopAdapter: CafeShopAdapter by lazy {
        CafeShopAdapter {
            navigateToFragment(
                R.id.action_cafeFragment_to_cafeDetailFragment,
                Bundle().apply { putParcelable(Constants.CAFE_KEY, it) }
            )
        }
    }

    override fun setupDataBinding() {
        binding.fragment = this
        binding.lifecycleOwner = this
    }

    override fun initAnything() {
        requireActivity().onBackPressedDispatcher.addCallback(
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    popToHomeFragment()
                }
            }
        )
    }

    override fun prepareData() { requestGetData() }

    private fun requestGetData() {
        var hasLocalData = false
        viewModel.onFetchAllCafes(
            onDataFromLocal = {
                if (!it.isNullOrEmpty()) {
                    hasLocalData = true
                    bindDataToCafeAdapter(it)
                } else {
                    showNetworkErrorLayout()
                }
                binding.swipeRefreshLayout.checkThenHide()
            },
            onFailedMessage = {
                if (!hasLocalData) requireContext().showToast(it)
            }
        )
    }

    private fun showNetworkErrorLayout() = binding.layoutNetworkError.apply {
        root.toggleNetworkErrorLayout(
            enableToggle = true, enableMargin = true, marginPx = getBottomNavigationHeight()
        ).also {
            btnTryAgain.setOnClickListener {
                it.setScaleAnimation { requestGetData() }
            }
        }
    }

    override fun initViews() {
        binding.apply {
            swipeRefreshLayout.apply {
                setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.gold))
                setOnRefreshListener { requestGetData() }
            }
            rvCafe.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = cafeShopAdapter
            }
        }
    }

    override fun observeViewModel() {
        observeNetworkResult(viewModel.cafe,
            onSuccess = { result ->
                binding.swipeRefreshLayout.checkThenHide()
                binding.layoutNetworkError.root.toggleNetworkErrorLayout(false)
                bindDataToCafeAdapter(result.data).also {
                    lifecycleScope.launch { viewModel.onSyncAllCafes(result.data) }
                }
            },
            onError = { requireContext().showToast(it) }
        )
    }


    private fun bindDataToCafeAdapter(result: List<CafeModel>) {
        arrayOf<String>(
            requireContext().getString(R.string.nearby_coffee_shop),
            requireContext().getString(R.string.other_coffee_shop)
        ).also { tittles ->
            cafeShopAdapter.items = viewModel.mapCafeModelToCafeViewItem(tittles, result).also {
                transformedData = it
            }
        }
    }

    fun onSearchChanged(name: String) {
        cafeShopAdapter.items = viewModel.filterListByName(name, transformedData)
    }
}