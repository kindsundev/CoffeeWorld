package kind.sun.dev.coffeeworld.view.fragment.more

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.databinding.FragmentMoreBinding
import kind.sun.dev.coffeeworld.view.adapter.more.MoreAdapter
import kind.sun.dev.coffeeworld.utils.dataset.MoreDataSet

@AndroidEntryPoint
class MoreFragment : Fragment(){

    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoreBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerViewOptions()
    }

    private fun initRecyclerViewOptions() = binding.rvMoreOptions.apply {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(requireContext())
        adapter = MoreAdapter().apply {
            items = MoreDataSet.getMoreFragmentOptions()
            onItemClickListener = { itemId ->
                navigateFragment(itemId)
            }
        }
    }

    private fun navigateFragment(id: MoreDataSet.Id) {
        when(id) {
            MoreDataSet.Id.TERMS_AND_CONDITIONS -> {
                loadFragment(R.id.action_moreFragment_to_termsConditionsFragment)
            }
            MoreDataSet.Id.EXPLORE -> {
                loadFragment(R.id.action_moreFragment_to_exploreFragment)
            }
            MoreDataSet.Id.HUNTER_FOR_DEALS -> {
                loadFragment(R.id.action_moreFragment_to_huntDealsFragment)
            }
            MoreDataSet.Id.ORDER_REVIEWS -> {
                loadFragment(R.id.action_moreFragment_to_orderReviewsFragment)
            }
            MoreDataSet.Id.CONTACT_AND_FEEDBACK -> {
                loadFragment(R.id.action_moreFragment_to_feedbackFragment)
            }
            MoreDataSet.Id.USER -> {
                 loadFragment(R.id.action_moreFragment_to_profileFragment)
            }
            MoreDataSet.Id.SECURITY -> {
                loadFragment(R.id.action_moreFragment_to_securityFragment)
            }
            MoreDataSet.Id.SETTINGS -> {
                loadFragment(R.id.action_moreFragment_to_settingsFragment)
            }
            MoreDataSet.Id.PAYMENT -> {
                loadFragment(R.id.action_moreFragment_to_paymentFragment)
            }
            else -> {}
        }
    }


    private fun loadFragment(id: Int) {
        findNavController().navigate(id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}