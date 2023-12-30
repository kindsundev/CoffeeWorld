package kind.sun.dev.coffeeworld.ui.fragment.cafe

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.base.BaseFragment
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.databinding.FragmentCafeBinding
import kind.sun.dev.coffeeworld.ui.adapter.cafe.CafeShopAdapter
import kind.sun.dev.coffeeworld.util.common.Constants
import kind.sun.dev.coffeeworld.util.helper.animation.setScaleAnimation
import kind.sun.dev.coffeeworld.util.helper.view.checkToHide
import kind.sun.dev.coffeeworld.util.helper.view.toggleNetworkErrorLayout
import kind.sun.dev.coffeeworld.ui.adapter.cafe.CafeShopViewItem
import kind.sun.dev.coffeeworld.viewmodel.CafeViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CafeFragment : BaseFragment<FragmentCafeBinding, CafeViewModel>(
    layoutInflater = FragmentCafeBinding::inflate,
    viewModelClass = CafeViewModel::class.java
) {
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

    override fun prepareData() {
        requestGetData()
    }

    private fun requestGetData(isSwipeToRefreshLoading: Boolean = false) {
        var hasLocalData = false
        lifecycleScope.launch {
            viewModel?.onFetchAllCafes(
                isLoading = isSwipeToRefreshLoading,
                onDataFromLocal = {
                    if (!it.isNullOrEmpty()) {
                        hasLocalData = true
                        bindDataToCafeAdapter(it)
                    } else {
                        showNetworkErrorLayout()
                    }
                    binding.swipeRefreshLayout.checkToHide()
                },
                onFailedMessage = {
                    if (!hasLocalData) {
                        StyleableToast.makeText(requireContext(), it, R.style.toast_network).show()
                    }
                }
            )
        }
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
                setOnRefreshListener { requestGetData(true) }
            }
            rvCafe.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = cafeShopAdapter
            }
        }
    }

    override fun observeViewModel() {
        viewModel!!.cafe.observeNetworkResult(
            onSuccess = { result ->
                binding.swipeRefreshLayout.checkToHide()
                binding.layoutNetworkError.root.toggleNetworkErrorLayout(false)
                bindDataToCafeAdapter(result.data).also {
                    lifecycleScope.launch { viewModel!!.onSyncAllCafes(result.data) }
                }
            },
            onError = {
                StyleableToast.makeText(requireContext(), it, R.style.toast_error).show()
            }
        )
    }

    private fun bindDataToCafeAdapter(result: List<CafeModel>) {
        arrayOf<String>(
            requireContext().getString(R.string.nearby_coffee_shop),
            requireContext().getString(R.string.other_coffee_shop)
        ).also { tittles ->
            transformedData = viewModel!!.convertToCafeViewItem(tittles, result)
            cafeShopAdapter.items = transformedData
        }
    }

    fun onSearchChanged(name: String) {
        if (viewModel == null) return
        cafeShopAdapter.items = viewModel!!.filterCafeList(name, transformedData)
    }
}