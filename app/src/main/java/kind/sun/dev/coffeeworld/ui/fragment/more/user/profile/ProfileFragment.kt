package kind.sun.dev.coffeeworld.ui.fragment.more.user.profile

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.base.BaseFragment
import kind.sun.dev.coffeeworld.data.local.model.UserModel
import kind.sun.dev.coffeeworld.databinding.FragmentProfileBinding
import kind.sun.dev.coffeeworld.util.common.Constants
import kind.sun.dev.coffeeworld.util.dataset.MoreDataSet
import kind.sun.dev.coffeeworld.util.helper.view.checkToHide
import kind.sun.dev.coffeeworld.util.helper.view.showAlertDialog
import kind.sun.dev.coffeeworld.util.helper.view.showToastError
import kind.sun.dev.coffeeworld.ui.adapter.profile.ProfileAdapter
import kind.sun.dev.coffeeworld.ui.dialog.profile.ProfileUpdateAddressDF
import kind.sun.dev.coffeeworld.ui.bsdf.user.ProfileUpdateAvatarBSDF
import kind.sun.dev.coffeeworld.ui.dialog.profile.ProfileUpdateEmailDF
import kind.sun.dev.coffeeworld.ui.dialog.profile.ProfileUpdateNameDF
import kind.sun.dev.coffeeworld.ui.dialog.profile.ProfileUpdatePasswordDF
import kind.sun.dev.coffeeworld.ui.dialog.profile.ProfileUpdatePhoneDF
import kind.sun.dev.coffeeworld.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, UserViewModel>(
    layoutInflater = FragmentProfileBinding::inflate,
    viewModelClass = UserViewModel::class.java
) {
    private lateinit var userModel: UserModel

    private val profileAdapter : ProfileAdapter by lazy {
        ProfileAdapter(MoreDataSet.getProfileFragmentOptions()) {
            onItemClickListener(it)
        }
    }

    override fun setupDataBinding() {
        binding.fragment = this
        binding.lifecycleOwner = this
    }

    override fun prepareData() { requestGetData() }

    private fun requestGetData(isSwipeToRefreshLoading: Boolean = false) {
        var hasLocalData = false
        viewModel?.onFetchUser(
            isLoading = isSwipeToRefreshLoading,
            onDataFromLocal = {
                it?.let {
                    notifyUser(it).also { hasLocalData = true }
                } ?: StyleableToast.makeText(
                    requireContext(), getString(R.string.you_are_offline), R.style.toast_network
                ).show()
                binding.swipeRefreshLayout.checkToHide()
            },
            onFailedMessage = {
                if (!hasLocalData)
                    StyleableToast.makeText(requireContext(), it, R.style.toast_network).show()
            }
        )
    }

    override fun initViews() {
        binding.apply {
            swipeRefreshLayout.apply {
                setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.gold))
                setOnRefreshListener { requestGetData(true) }
            }
        }
        binding.rvProfileOptions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = profileAdapter
        }
    }

    override fun observeViewModel() {
        viewModel!!.userResponse.observeNetworkResult(
            onSuccess = { result ->
                binding.swipeRefreshLayout.checkToHide()
                notifyUser(result.data).also {
                    lifecycleScope.launch { viewModel!!.onSyncUser(result.data) }
                }
            },
            onError = { requireContext().showToastError(it) }
        )
    }

    private fun onItemClickListener(itemId: MoreDataSet.Id) {
        when(itemId) {
            MoreDataSet.Id.AVATAR -> ProfileUpdateAvatarBSDF(::onUpdateSuccess).show(childFragmentManager, null)
            MoreDataSet.Id.NAME -> ProfileUpdateNameDF(::onUpdateSuccess).show(childFragmentManager, null)
            MoreDataSet.Id.EMAIL -> ProfileUpdateEmailDF(::onUpdateSuccess).show(childFragmentManager, null)
            MoreDataSet.Id.ADDRESS -> ProfileUpdateAddressDF(::onUpdateSuccess).show(childFragmentManager, null)
            MoreDataSet.Id.PHONE -> ProfileUpdatePhoneDF(::onUpdateSuccess).show(childFragmentManager, null)
            MoreDataSet.Id.PASSWORD -> ProfileUpdatePasswordDF().show(childFragmentManager, null)
            MoreDataSet.Id.LOG_OUT -> onClickLogout()
            MoreDataSet.Id.PROFILE -> {
                navigateToFragment(
                    R.id.action_profileFragment_to_profileDetailFragment,
                    Bundle().apply { putParcelable(Constants.USER_KEY, userModel) }
                )
            }
            else -> {}
        }
    }

    private fun onClickLogout() {
        requireContext().showAlertDialog(R.string.logout, R.string.confirm_logout) {
            preferences.userToken = null
            navigateToFragment(R.id.action_profileFragment_to_loginFragment)
        }
    }

    fun onBackToMoreFragment() : Unit = popFragment()

    private fun onUpdateSuccess(message: String) {
        StyleableToast.makeText(requireContext(), message, R.style.toast_success).show()
        viewModel!!.onFetchUser(
            isLoading = true,
            onDataFromLocal = {
                it?.let { notifyUser(it) }
            },
            onFailedMessage = {
                StyleableToast.makeText(requireContext(), it, R.style.toast_error).show()
            }
        )
    }

    private fun notifyUser(result: UserModel) {
        profileAdapter.user = result.also { userModel = it }
    }
}