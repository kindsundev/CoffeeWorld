package kind.sun.dev.coffeeworld.view.fragment.cafe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.data.model.response.cafe.CafeModel
import kind.sun.dev.coffeeworld.databinding.FragmentCafeBinding
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.common.Logger
import kind.sun.dev.coffeeworld.utils.custom.CustomLoadingDialog
import kind.sun.dev.coffeeworld.view.adapter.cafe.CafeShopAdapter
import kind.sun.dev.coffeeworld.view.adapter.cafe.CafeShopViewItem
import kind.sun.dev.coffeeworld.viewmodel.CafeViewModel
import javax.inject.Inject

@AndroidEntryPoint
class CafeFragment : Fragment() {
    private var _binding: FragmentCafeBinding? = null
    private val binding get() = _binding!!

    private val cafeViewModel by viewModels<CafeViewModel>()
    @Inject lateinit var loadingDialog: CustomLoadingDialog
    private lateinit var cafeShopAdapter: CafeShopAdapter
    private lateinit var transformedData: List<CafeShopViewItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCafeBinding.inflate(inflater, container, false).apply {
            fragment = this@CafeFragment
            viewModel = cafeViewModel
            lifecycleOwner = this@CafeFragment
        }
        cafeViewModel.getCafeList()
        setupCafeRecyclerView()
        return binding.root
    }

    private fun setupCafeRecyclerView() = binding.rvCafe.apply {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(requireContext())
        adapter = CafeShopAdapter(::onCafeClicked).also {
            cafeShopAdapter = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCafeObservers()
        // todo: scale backend to: cafe shop, cafe bar, cafe house (here implement viewpager)
    }

    private fun setupCafeObservers() {
        cafeViewModel.cafe.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkResult.Success -> {
                    if (loadingDialog.isAdded) {
                        loadingDialog.dismiss()
                        it.data?.data?.let { result ->
                            initDataToCafeAdapter(result)
                        }
                    }
                }
                is NetworkResult.Error -> {
                    if (loadingDialog.isAdded) {
                        loadingDialog.dismiss()
                        Logger.error(it.message.toString())
                    }
                }
                is NetworkResult.Loading -> {
                    loadingDialog.show(childFragmentManager, CustomLoadingDialog::class.simpleName)
                }
            }
        }
    }

    private fun initDataToCafeAdapter(result: List<CafeModel>) {
        arrayOf<String>(
            requireContext().getString(R.string.nearby_coffee_shop),
            requireContext().getString(R.string.other_coffee_shop)
        ).also { tittles ->
            cafeShopAdapter.items = cafeViewModel.mapCafeModelToCafeViewItem(tittles, result)
                .also {
                    transformedData = it
                }
        }
    }

    fun onSearchChanged(name: String) {
        cafeShopAdapter.items = cafeViewModel.filterListByName(name, transformedData)
    }

    private fun onCafeClicked(cafe: CafeModel) {
        findNavController().navigate(
            R.id.action_cafeFragment_to_cafeDetailFragment,
            Bundle().apply {
                putParcelable(Constants.CAFE_KEY, cafe)
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}