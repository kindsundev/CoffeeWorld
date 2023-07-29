package kind.sun.dev.coffeeworld.ui.cafe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.data.model.response.cafe.CafeModel
import kind.sun.dev.coffeeworld.databinding.FragmentCafeBinding
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.common.Logger
import kind.sun.dev.coffeeworld.utils.view.LoadingDialog
import javax.inject.Inject

@AndroidEntryPoint
class CafeFragment : Fragment() {
    private var _binding: FragmentCafeBinding? = null
    private val binding get() = _binding!!

    private val cafeViewModel by viewModels<CafeViewModel>()
    @Inject lateinit var loadingDialog: LoadingDialog
    private lateinit var cafeAdapter: CafeAdapter
    private lateinit var originalListCafe : List<CafeModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCafeBinding.inflate(inflater, container, false)
        cafeAdapter = CafeAdapter(::onCafeClicked)
        setupDataBinding()
        return binding.root
    }

    private fun setupDataBinding() {
        binding.apply {
            fragment = this@CafeFragment
            viewModel = cafeViewModel
            lifecycleOwner = this@CafeFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cafeViewModel.getListCafe()
        setupListCafeLiveData()
    }

    private fun setupListCafeLiveData() {
        cafeViewModel.cafeResponseLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkResult.Success -> {
                    loadingDialog.dismiss()
                    showBodySection(true)
                    it.data?.data?.let { list ->
                        originalListCafe = list
                        initCoffeeNearHere(cafe = list[0])
                        initRecyclerViewCafe(list.subList(1, list.size))
                    }
                }
                is NetworkResult.Error -> {
                    loadingDialog.dismiss()
                    Logger.error(it.message.toString())
                }
                is NetworkResult.Loading -> {
                    loadingDialog.show(parentFragmentManager, LoadingDialog::class.simpleName)
                }
            }
        }
    }

    private fun initCoffeeNearHere(cafe: CafeModel) {
        binding.layoutCoffeeShopNear.apply {
            tvName.text = cafe.name
            tvLocation.text = cafe.location
            root.setOnClickListener { onCafeClicked(cafe) }
            Glide.with(requireContext())
                .load(cafe.image)
                .placeholder(R.drawable.img_coffee_loading)
                .centerCrop()
                .into(imgCafe)
        }
    }

    private fun initRecyclerViewCafe(list: List<CafeModel>) {
        cafeAdapter.let {
            binding.rvCafe.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = it
            }
            it.submitList(list)
        }
    }

    private fun onCafeClicked(cafe: CafeModel) {
        val bundle = Bundle().apply {
            putSerializable(Constants.CAFE_KEY, cafe)
        }
        findNavController().navigate(R.id.action_cafeFragment_to_cafeDetailFragment, bundle)
    }

    fun onSearchChanged(name: String) {
        val searchResult = cafeViewModel.filterListByName(name, originalListCafe)
        if (name.isNotBlank()) {
            showBodySection(false)
            binding.rvCafe.visibility = View.VISIBLE
            cafeAdapter.submitList(searchResult)
        } else {
            showBodySection(true)
            cafeAdapter.submitList(originalListCafe)
        }
    }

    private fun showBodySection(visible : Boolean) {
        binding.apply {
            if (visible) {
                tvCoffeeShopNear.visibility = View.VISIBLE
                tvOtherCoffee.visibility = View.VISIBLE
                layoutCoffeeShopNear.root.visibility = View.VISIBLE
                rvCafe.visibility = View.VISIBLE
            } else {
                tvCoffeeShopNear.visibility = View.GONE
                tvOtherCoffee.visibility = View.GONE
                layoutCoffeeShopNear.root.visibility = View.GONE
                rvCafe.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}