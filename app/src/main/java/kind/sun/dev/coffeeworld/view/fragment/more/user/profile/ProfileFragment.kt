package kind.sun.dev.coffeeworld.view.fragment.more.user.profile

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.base.BaseFragment
import kind.sun.dev.coffeeworld.data.local.model.UserModel
import kind.sun.dev.coffeeworld.databinding.FragmentProfileBinding
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.common.Logger
import kind.sun.dev.coffeeworld.utils.dataset.MoreDataSet
import kind.sun.dev.coffeeworld.utils.helper.view.checkToHide
import kind.sun.dev.coffeeworld.utils.helper.view.showAlertDialog
import kind.sun.dev.coffeeworld.utils.helper.view.showToastError
import kind.sun.dev.coffeeworld.view.adapter.profile.ProfileAdapter
import kind.sun.dev.coffeeworld.view.dialog.profile.AddressDialogFragment
import kind.sun.dev.coffeeworld.view.dialog.profile.AvatarBottomFragment
import kind.sun.dev.coffeeworld.view.dialog.profile.EmailDialogFragment
import kind.sun.dev.coffeeworld.view.dialog.profile.NameDialogFragment
import kind.sun.dev.coffeeworld.view.dialog.profile.PasswordDialogFragment
import kind.sun.dev.coffeeworld.view.dialog.profile.PhoneDialogFragment
import kind.sun.dev.coffeeworld.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, UserViewModel>(
    FragmentProfileBinding::inflate
) {
    private lateinit var userModel: UserModel

    override val viewModel: UserViewModel by viewModels()

    private val profileAdapter : ProfileAdapter by lazy {
        ProfileAdapter(MoreDataSet.getProfileFragmentOptions()) {
            onItemClickListener(it)
        }
    }

    override fun setupDataBinding() {
        binding.apply {
            fragment = this@ProfileFragment
            lifecycleOwner = this@ProfileFragment
        }
    }

    override fun prepareData() { requestGetData() }

    private fun requestGetData(isSwipeToRefreshLoading: Boolean = false) {
        var hasLocalData = false
        viewModel.onFetchUser(
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
        viewModel.userResponse.observeNetworkResult(
            onSuccess = { result ->
                binding.swipeRefreshLayout.checkToHide()
                notifyUser(result.data).also {
                    lifecycleScope.launch { viewModel.onSyncUser(result.data) }
                }
            },
            onError = { requireContext().showToastError(it) }
        )
    }

    private fun onItemClickListener(itemId: MoreDataSet.Id) {
        when(itemId) {
            MoreDataSet.Id.AVATAR -> AvatarBottomFragment(::onUpdateSuccess).show(childFragmentManager, null)
            MoreDataSet.Id.NAME -> NameDialogFragment(::onUpdateSuccess).show(childFragmentManager, null)
            MoreDataSet.Id.EMAIL -> EmailDialogFragment(::onUpdateSuccess).show(childFragmentManager, null)
            MoreDataSet.Id.ADDRESS -> AddressDialogFragment(::onUpdateSuccess).show(childFragmentManager, null)
            MoreDataSet.Id.PHONE -> PhoneDialogFragment(::onUpdateSuccess).show(childFragmentManager, null)
            MoreDataSet.Id.PASSWORD -> PasswordDialogFragment().show(childFragmentManager, null)
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
        viewModel.onFetchUser(
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