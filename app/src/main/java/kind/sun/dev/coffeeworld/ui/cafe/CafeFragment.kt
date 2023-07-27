package kind.sun.dev.coffeeworld.ui.cafe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.data.model.response.cafe.CafeModel
import kind.sun.dev.coffeeworld.databinding.FragmentCafeBinding
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCafeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cafeViewModel.getListCafe()
        setupInternetLiveData()
        setupListCafeLiveData()
    }

    private fun setupInternetLiveData() {
        cafeViewModel.noInternetLiveData.observe(viewLifecycleOwner) { isDisconnected ->
            if (isDisconnected) {
                binding.llBody.visibility = View.GONE
                binding.imgOffline.visibility = View.VISIBLE
            } else {
                binding.imgOffline.visibility = View.GONE
                binding.llBody.visibility = View.VISIBLE
            }
        }
    }

    private fun setupListCafeLiveData() {
        cafeViewModel.cafeResponseLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkResult.Success -> {
                    loadingDialog.dismiss()
                    binding.llBody.visibility = View.VISIBLE
                    initRecyclerView(it.data!!.data)
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

    private fun initRecyclerView(list: List<CafeModel>) {
        cafeAdapter = CafeAdapter(::onCafeClicked)
        cafeAdapter.let {
            binding.rvCafe.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = cafeAdapter
            }
            initCoffeeNearHere(list[0])
            it.submitList(list.subList(1, list.size))
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

    private fun onCafeClicked(cafe: CafeModel) {
        Toast.makeText(requireContext(), cafe.id.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}