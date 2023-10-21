package kind.sun.dev.coffeeworld.view.fragment.cafe

import android.os.Bundle
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
            })
    }

    override fun prepareData() {
        // todo: switch layout offline and pull-to-refresh
        var hasLocalData = false
        viewModel.onFetchAllCafes(
            onDataFromLocal = {
                it?.let {
                    bindDataToCafeAdapter(it).also { hasLocalData = true }
                } ?: requireContext().showToast(getString(R.string.you_are_offline))
            }
        ) { reason ->
            if (!hasLocalData) requireContext().showToast(reason)
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