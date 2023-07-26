package kind.sun.dev.coffeeworld.ui.cafe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCafeBinding.inflate(layoutInflater)
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
                    Logger.info(it.data!!.data.size.toString())
                    TODO("Implementation adapter")
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}