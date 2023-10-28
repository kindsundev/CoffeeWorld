package kind.sun.dev.coffeeworld.view.fragment.cafe

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
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
import kind.sun.dev.coffeeworld.utils.helper.view.showToast
import kind.sun.dev.coffeeworld.view.adapter.cafe.CafeShopAdapter
import kind.sun.dev.coffeeworld.view.adapter.cafe.CafeShopViewItem
import kind.sun.dev.coffeeworld.viewmodel.CafeViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CafeFragment : BaseFragment<FragmentCafeBinding, CafeViewModel>(
    FragmentCafeBinding::inflate
){
    override val viewModel: CafeViewModel by viewModels()

    private val cafeShopAdapter: CafeShopAdapter by lazy {
        CafeShopAdapter {
            navigateToFragment(
                R.id.action_cafeFragment_to_cafeDetailFragment,
                Bundle().apply { putParcelable(Constants.CAFE_KEY, it) }
            )
        }
    }

    private lateinit var transformedData: List<CafeShopViewItem>

    override fun setupDataBinding() {
        binding.apply {
            fragment = this@CafeFragment
            lifecycleOwner = this@CafeFragment
        }
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
            },
            onFailedMessage = { if (!hasLocalData) requireContext().showToast(it) }
        )
    }

    private fun showNetworkErrorLayout() = binding.apply {
        toggleNetworkErrorLayout(true).also {
            layoutNetworkError.btnTryAgain.setOnClickListener {
                it.setScaleAnimation { requestGetData() }
            }
        }
    }

    override fun initViews() {
        binding.rvCafe.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cafeShopAdapter
        }
    }

    override fun observeViewModel() {
        observeNetworkResult(viewModel.cafe,
            onSuccess = { result ->
                toggleNetworkErrorLayout(false)
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

    private fun toggleNetworkErrorLayout(enable: Boolean) = binding.layoutNetworkError.root.apply {
        if (enable) {
            layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
                setMargins(0, 0, 0, getBottomNavigationHeight())
            }.also {
                visibility = View.VISIBLE
            }
        } else {
            visibility = View.GONE
        }
    }
}