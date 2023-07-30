package kind.sun.dev.coffeeworld.ui.more

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.databinding.FragmentMoreBinding

@AndroidEntryPoint
class MoreFragment : Fragment() {

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
        initUtilityListeners()
        initSupportListeners()
        initProfileListeners()
    }

    private fun initUtilityListeners() {
        binding.utility.apply {
            explore.setOnClickListener {
                loadFragment(R.id.action_moreFragment_to_exploreFragment)
            }
            huntForDeals.setOnClickListener {
                loadFragment(R.id.action_moreFragment_to_huntDealsFragment)
            }
            termsAndConditions.setOnClickListener {
                loadFragment(R.id.action_moreFragment_to_termsConditionsFragment)
            }
        }
    }

    private fun initSupportListeners() {
        binding.support.apply {
            contactAndFeedback.setOnClickListener {
                loadFragment(R.id.action_moreFragment_to_feedbackFragment)
            }
            orderReview.setOnClickListener {
                loadFragment(R.id.action_moreFragment_to_orderReviewsFragment)
            }
        }
    }

    private fun initProfileListeners() {
        binding.account.apply {
            info.setOnClickListener {
                loadFragment(R.id.action_moreFragment_to_profileFragment)
            }
            security.setOnClickListener {
                loadFragment(R.id.action_moreFragment_to_securityFragment)
            }
            settings.setOnClickListener {
                loadFragment(R.id.action_moreFragment_to_settingsFragment)
            }
            logout.setOnClickListener { onClickLogout() }
        }
    }

    private fun loadFragment(id: Int) {
        findNavController().navigate(id)
    }

    private fun onClickLogout() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}