package kind.sun.dev.coffeeworld.view.fragment.cafe

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.base.BaseFragment
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.databinding.FragmentCafeBinding
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.common.Logger
import kind.sun.dev.coffeeworld.view.adapter.cafe.CafeShopAdapter
import kind.sun.dev.coffeeworld.view.adapter.cafe.CafeShopViewItem
import kind.sun.dev.coffeeworld.viewmodel.CafeViewModel

@AndroidEntryPoint
class CafeFragment : BaseFragment<FragmentCafeBinding, CafeViewModel>(
    FragmentCafeBinding::inflate
){
    override val viewModel: CafeViewModel by viewModels()
    private lateinit var cafeShopAdapter: CafeShopAdapter
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

    override fun prepareData(): Unit = viewModel.getCafeList()

    override fun initViews() {
        binding.rvCafe.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = CafeShopAdapter(::onCafeClicked).also { cafeShopAdapter = it }
        }
    }

    private fun onCafeClicked(cafe: CafeModel) {
        navigateToFragment(R.id.action_cafeFragment_to_cafeDetailFragment, Bundle().apply {
            putParcelable(Constants.CAFE_KEY, cafe)
        })
    }

    override fun observeViewModel() {
        observeNetworkResult(viewModel.cafe,
            onSuccess = { initDataToCafeAdapter(it.data) },
            onError = { Logger.error("[CafeFragment]: $it") }
        )
    }

    private fun initDataToCafeAdapter(result: List<CafeModel>) {
        arrayOf<String>(
            requireContext().getString(R.string.nearby_coffee_shop),
            requireContext().getString(R.string.other_coffee_shop)
        ).also { tittles ->
            cafeShopAdapter.items = viewModel.mapCafeModelToCafeViewItem(tittles, result)
                .also {
                    transformedData = it
                }
        }
    }

    fun onSearchChanged(name: String) {
        cafeShopAdapter.items = viewModel.filterListByName(name, transformedData)
    }
}