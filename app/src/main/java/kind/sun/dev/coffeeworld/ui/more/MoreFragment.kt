package kind.sun.dev.coffeeworld.ui.more

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
import kind.sun.dev.coffeeworld.ui.more.adapter.MoreRecyclerViewAdapter
import kind.sun.dev.coffeeworld.utils.data.MoreOptionUtils

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
        adapter = MoreRecyclerViewAdapter().apply {
            items = MoreOptionUtils.getMoreOptions()
            onItemClickListener = { itemId ->
                navigateFragment(itemId)
            }
        }
    }

    private fun navigateFragment(id: MoreOptionUtils.Id) {
        when(id) {
            MoreOptionUtils.Id.TERMS_AND_CONDITIONS -> {
                loadFragment(R.id.action_moreFragment_to_termsConditionsFragment)
            }
            MoreOptionUtils.Id.EXPLORE -> {
                loadFragment(R.id.action_moreFragment_to_exploreFragment)
            }
            MoreOptionUtils.Id.HUNTER_FOR_DEALS -> {
                loadFragment(R.id.action_moreFragment_to_huntDealsFragment)
            }
            MoreOptionUtils.Id.ORDER_REVIEWS -> {
                loadFragment(R.id.action_moreFragment_to_orderReviewsFragment)
            }
            MoreOptionUtils.Id.CONTACT_AND_FEEDBACK -> {
                loadFragment(R.id.action_moreFragment_to_feedbackFragment)
            }
            MoreOptionUtils.Id.USER -> {
                 loadFragment(R.id.action_moreFragment_to_profileFragment)
            }
            MoreOptionUtils.Id.SECURITY -> {
                loadFragment(R.id.action_moreFragment_to_securityFragment)
            }
            MoreOptionUtils.Id.SETTINGS -> {
                loadFragment(R.id.action_moreFragment_to_settingsFragment)
            }
            MoreOptionUtils.Id.PAYMENT -> {
                loadFragment(R.id.action_moreFragment_to_paymentFragment)
            }
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