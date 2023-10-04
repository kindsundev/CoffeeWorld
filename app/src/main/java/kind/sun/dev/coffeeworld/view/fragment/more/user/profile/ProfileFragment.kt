package kind.sun.dev.coffeeworld.view.fragment.more.user.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.data.model.response.auth.UserModel
import kind.sun.dev.coffeeworld.databinding.FragmentProfileBinding
import kind.sun.dev.coffeeworld.view.adapter.profile.ProfileAdapter
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.helper.storage.TokenPreferencesHelper
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.common.Logger
import kind.sun.dev.coffeeworld.utils.dataset.MoreDataSet
import kind.sun.dev.coffeeworld.utils.custom.CustomLoadingDialog
import kind.sun.dev.coffeeworld.utils.helper.view.showAlertDialog
import kind.sun.dev.coffeeworld.view.dialog.profile.AddressDialogFragment
import kind.sun.dev.coffeeworld.view.dialog.profile.AvatarBottomFragment
import kind.sun.dev.coffeeworld.view.dialog.profile.EmailDialogFragment
import kind.sun.dev.coffeeworld.view.dialog.profile.NameDialogFragment
import kind.sun.dev.coffeeworld.view.dialog.profile.PasswordDialogFragment
import kind.sun.dev.coffeeworld.view.dialog.profile.PhoneDialogFragment
import kind.sun.dev.coffeeworld.viewmodel.ProfileViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel by viewModels<ProfileViewModel>()
    @Inject lateinit var loadingDialog: CustomLoadingDialog

    private lateinit var userModel: UserModel
    @Inject lateinit var tokenManager: TokenPreferencesHelper
    private lateinit var profileAdapter : ProfileAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.fragment = this
        setupUserLiveData()
        return binding.root
    }

    private fun setupUserLiveData() {
        profileViewModel.userResponseLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkResult.Success -> {
                    if (loadingDialog.isAdded) {
                        loadingDialog.dismiss()
                        userModel = it.data!!.data
                        profileAdapter.user = userModel
                    }
                }
                is NetworkResult.Error -> {
                    if (loadingDialog.isAdded) {
                        loadingDialog.dismiss()
                        Logger.error(it.message.toString())
                    }
                }
                is NetworkResult.Loading -> {
                    loadingDialog.show(childFragmentManager, CustomLoadingDialog::class.simpleName)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.getUser()
        initRecyclerViewOptions()
    }

    private fun initRecyclerViewOptions() = binding.rvProfileOptions.apply {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = ProfileAdapter(MoreDataSet.getProfileFragmentOptions()) { id ->
            onItemClickListener(id)
        }.also {
            profileAdapter = it
        }
    }

    private fun onItemClickListener(itemId: MoreDataSet.Id) {
        when(itemId) {
            MoreDataSet.Id.PROFILE -> {
                findNavController().navigate(
                    R.id.action_profileFragment_to_profileDetailBottomFragment,
                    Bundle().apply { putParcelable(Constants.USER_KEY, userModel) }
                )
            }
            MoreDataSet.Id.AVATAR -> {
                AvatarBottomFragment(::onUpdateSuccess).show(childFragmentManager, null)
            }
            MoreDataSet.Id.NAME -> {
                NameDialogFragment(::onUpdateSuccess).show(childFragmentManager, null)
            }
            MoreDataSet.Id.EMAIL -> {
                EmailDialogFragment(::onUpdateSuccess).show(childFragmentManager, null)
            }
            MoreDataSet.Id.ADDRESS -> {
                AddressDialogFragment(::onUpdateSuccess).show(childFragmentManager, null)
            }
            MoreDataSet.Id.PHONE -> {
                PhoneDialogFragment(::onUpdateSuccess).show(childFragmentManager, null)
            }
            MoreDataSet.Id.PASSWORD -> {
                PasswordDialogFragment().show(childFragmentManager, null)
            }
            MoreDataSet.Id.LOG_OUT -> { onClickLogout() }
            MoreDataSet.Id.SWITCH_ACCOUNT -> {}
            else -> {}
        }
    }

    private fun onClickLogout() {
        showAlertDialog(requireContext(), "Logout", "Are you sure you want to log out?") {
            tokenManager.removeToken()
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }

    fun onBackToMoreFragment() { findNavController().popBackStack() }

    private fun onUpdateSuccess() : Unit = profileViewModel.getUser()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}