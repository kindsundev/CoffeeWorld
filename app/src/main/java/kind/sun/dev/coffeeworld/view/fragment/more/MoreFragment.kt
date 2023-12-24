package kind.sun.dev.coffeeworld.view.fragment.more

import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.base.BaseFragment
import kind.sun.dev.coffeeworld.databinding.FragmentMoreBinding
import kind.sun.dev.coffeeworld.utils.dataset.MoreDataSet
import kind.sun.dev.coffeeworld.view.adapter.more.MoreAdapter

@AndroidEntryPoint
class MoreFragment : BaseFragment<FragmentMoreBinding, Nothing>(
    layoutInflater = FragmentMoreBinding::inflate,
    viewModelClass = null
){

    override fun initAnything() {
        requireActivity().onBackPressedDispatcher.addCallback(
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    popToHomeFragment()
                }
            }
        )
    }

    override fun initViews() {
        binding.rvMoreOptions.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = MoreAdapter().apply {
                items = MoreDataSet.getMoreFragmentOptions()
                onItemClickListener = { itemId -> navigateFragment(itemId) }
            }
        }
    }

    private fun navigateFragment(id: MoreDataSet.Id) {
        when(id) {
            MoreDataSet.Id.TERMS_AND_CONDITIONS -> {
                navigateToFragment(R.id.action_moreFragment_to_termsConditionsFragment)
            }
            MoreDataSet.Id.EXPLORE -> {
                navigateToFragment(R.id.action_moreFragment_to_exploreFragment)
            }
            MoreDataSet.Id.HUNTER_FOR_DEALS -> {
                navigateToFragment(R.id.action_moreFragment_to_huntDealsFragment)
            }
            MoreDataSet.Id.ORDER_REVIEWS -> {
                navigateToFragment(R.id.action_moreFragment_to_orderReviewsFragment)
            }
            MoreDataSet.Id.CONTACT_AND_FEEDBACK -> {
                navigateToFragment(R.id.action_moreFragment_to_feedbackFragment)
            }
            MoreDataSet.Id.USER -> {
                 navigateToFragment(R.id.action_moreFragment_to_profileFragment)
            }
            MoreDataSet.Id.SECURITY -> {
                navigateToFragment(R.id.action_moreFragment_to_securityFragment)
            }
            MoreDataSet.Id.SETTINGS -> {
                navigateToFragment(R.id.action_moreFragment_to_settingsFragment)
            }
            MoreDataSet.Id.PAYMENT -> {
                navigateToFragment(R.id.action_moreFragment_to_paymentFragment)
            }
            else -> {}
        }
    }
}