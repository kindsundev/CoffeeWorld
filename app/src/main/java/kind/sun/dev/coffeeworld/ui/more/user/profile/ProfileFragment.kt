package kind.sun.dev.coffeeworld.ui.more.user.profile

import android.content.Intent
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
import kind.sun.dev.coffeeworld.ui.auth.AuthActivity
import kind.sun.dev.coffeeworld.ui.more.user.profile.adapter.ProfileAdapter
import kind.sun.dev.coffeeworld.ui.more.user.profile.address.AddressDialogFragment
import kind.sun.dev.coffeeworld.ui.more.user.profile.avatar.AvatarBottomFragment
import kind.sun.dev.coffeeworld.ui.more.user.profile.email.EmailDialogFragment
import kind.sun.dev.coffeeworld.ui.more.user.profile.name.NameDialogFragment
import kind.sun.dev.coffeeworld.ui.more.user.profile.password.PasswordDialogFragment
import kind.sun.dev.coffeeworld.ui.more.user.profile.phone.PhoneDialogFragment
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.api.TokenManager
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.common.Logger
import kind.sun.dev.coffeeworld.utils.data.MoreOptionUtils
import kind.sun.dev.coffeeworld.utils.view.LoadingDialog
import kind.sun.dev.coffeeworld.utils.view.showAlertDialog
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel by viewModels<ProfileViewModel>()
    @Inject lateinit var loadingDialog: LoadingDialog

    private lateinit var userModel: UserModel
    @Inject lateinit var tokenManager: TokenManager
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
                    loadingDialog.show(childFragmentManager, LoadingDialog::class.simpleName)
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
        adapter = ProfileAdapter(MoreOptionUtils.getProfileFragmentOptions()) { id ->
            onItemClickListener(id)
        }.also {
            profileAdapter = it
        }
    }

    private fun onItemClickListener(itemId: MoreOptionUtils.Id) {
        when(itemId) {
            MoreOptionUtils.Id.PROFILE -> {
                findNavController().navigate(
                    R.id.action_profileFragment_to_profileDetailBottomFragment,
                    Bundle().apply { putParcelable(Constants.USER_KEY, userModel) }
                )
            }
            MoreOptionUtils.Id.AVATAR -> {
                AvatarBottomFragment(::onUpdateSuccess).show(childFragmentManager, null)
            }
            MoreOptionUtils.Id.NAME -> {
                NameDialogFragment(::onUpdateSuccess).show(childFragmentManager, null)
            }
            MoreOptionUtils.Id.EMAIL -> {
                EmailDialogFragment(::onUpdateSuccess).show(childFragmentManager, null)
            }
            MoreOptionUtils.Id.ADDRESS -> {
                AddressDialogFragment(::onUpdateSuccess).show(childFragmentManager, null)
            }
            MoreOptionUtils.Id.PHONE -> {
                PhoneDialogFragment(::onUpdateSuccess).show(childFragmentManager, null)
            }
            MoreOptionUtils.Id.PASSWORD -> {
                PasswordDialogFragment().show(childFragmentManager, null)
            }
            MoreOptionUtils.Id.LOG_OUT -> { onClickLogout() }
            MoreOptionUtils.Id.SWITCH_ACCOUNT -> {}
            else -> {}
        }
    }

    private fun onClickLogout() {
        showAlertDialog(requireContext(), "Logout", "Are you sure you want to log out?") {
            tokenManager.removeToken()
            startActivity(Intent(requireContext(), AuthActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK )
            })
        }
    }

    fun onBackToMoreFragment() { findNavController().popBackStack() }

    private fun onUpdateSuccess() : Unit = profileViewModel.getUser()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}