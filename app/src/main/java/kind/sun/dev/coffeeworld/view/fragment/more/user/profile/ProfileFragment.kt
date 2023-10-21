package kind.sun.dev.coffeeworld.view.fragment.more.user.profile

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.base.BaseFragment
import kind.sun.dev.coffeeworld.data.local.model.UserModel
import kind.sun.dev.coffeeworld.databinding.FragmentProfileBinding
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.dataset.MoreDataSet
import kind.sun.dev.coffeeworld.utils.helper.view.showAlertDialog
import kind.sun.dev.coffeeworld.utils.helper.view.showToast
import kind.sun.dev.coffeeworld.view.adapter.profile.ProfileAdapter
import kind.sun.dev.coffeeworld.view.dialog.profile.AddressDialogFragment
import kind.sun.dev.coffeeworld.view.dialog.profile.AvatarBottomFragment
import kind.sun.dev.coffeeworld.view.dialog.profile.EmailDialogFragment
import kind.sun.dev.coffeeworld.view.dialog.profile.NameDialogFragment
import kind.sun.dev.coffeeworld.view.dialog.profile.PasswordDialogFragment
import kind.sun.dev.coffeeworld.view.dialog.profile.PhoneDialogFragment
import kind.sun.dev.coffeeworld.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>(
    FragmentProfileBinding::inflate
) {
    private lateinit var userModel: UserModel

    override val viewModel: ProfileViewModel by viewModels()

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

    override fun prepareData() {
        var hasLocalData = false
        viewModel.onFetchUser(
            onDataFromLocal = {
                it?.let {
                    notifyUser(it).also { hasLocalData = true }
                } ?: requireContext().showToast(getString(R.string.you_are_offline))
            }
        ) { reason ->
            if (!hasLocalData) requireContext().showToast(reason)
        }
    }

    override fun initViews() {
        binding.rvProfileOptions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = profileAdapter
        }
    }

    override fun observeViewModel() {
        observeNetworkResult(viewModel.userResponse,
            onSuccess = { result ->
                notifyUser(result.data).also {
                    lifecycleScope.launch { viewModel.onSyncUser(result.data) }
                }
            },
            onError = { requireContext().showToast(it) }
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
        showAlertDialog(requireContext(), R.string.logout, R.string.confirm_logout) {
            preferences.userToken = null
            navigateToFragment(R.id.action_profileFragment_to_loginFragment)
        }
    }

    fun onBackToMoreFragment() : Unit = popFragment()

    private fun onUpdateSuccess() {
        viewModel.onFetchUser(
            onDataFromLocal = {
                it?.let { notifyUser(it) }
            }
        ) { reason ->
            requireContext().showToast(reason)
        }
    }

    private fun notifyUser(result: UserModel) {
        profileAdapter.user = result.also { userModel = it }
    }
}