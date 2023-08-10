package kind.sun.dev.coffeeworld.ui.more.user.profile

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.databinding.FragmentProfileBinding
import kind.sun.dev.coffeeworld.ui.more.user.profile.address.AddressDialogFragment
import kind.sun.dev.coffeeworld.ui.more.user.profile.avatar.AvatarBottomFragment
import kind.sun.dev.coffeeworld.ui.more.user.profile.email.EmailDialogFragment
import kind.sun.dev.coffeeworld.ui.more.user.profile.name.NameDialogFragment
import kind.sun.dev.coffeeworld.ui.more.user.profile.phone.PhoneDialogFragment
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.common.Logger
import kind.sun.dev.coffeeworld.utils.view.LoadingDialog
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(), ProfileUpdateCallback {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel by viewModels<ProfileViewModel>()
    @Inject lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDataBinding()
        profileViewModel.getUser()
        setupUserLiveData()
    }

    private fun setupDataBinding() {
        binding.apply {
            fragment = this@ProfileFragment
            user.fragment = this@ProfileFragment
            dashboard.fragment = this@ProfileFragment
        }
    }

    private fun setupUserLiveData() {
        profileViewModel.userResponseLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkResult.Success -> {
                    loadingDialog.dismiss()
                    val user = it.data!!.data
                    binding.user.userModel = user
                }
                is NetworkResult.Error -> {
                    loadingDialog.dismiss()
                    Logger.error(it.message.toString())
                }
                is NetworkResult.Loading -> {
                    loadingDialog.show(childFragmentManager, LoadingDialog::class.simpleName)
                }
            }
        }
    }

    fun onShowAvatarFragment() : Unit = AvatarBottomFragment(this).show(childFragmentManager, null)

    fun onShowNameDialog() : Unit = NameDialogFragment(this).show(childFragmentManager, null)

    fun onShowAddressDialog() : Unit = AddressDialogFragment().show(childFragmentManager, null)

    fun onShowPhoneDialog() : Unit = PhoneDialogFragment().show(childFragmentManager, null)

    fun onShowEmailDialog() : Unit = EmailDialogFragment().show(childFragmentManager, null)

    fun onShowPasswordDialog() {

    }

    fun onShowProfileDetailFragment() {

    }

    fun onBackToMoreFragment() { findNavController().popBackStack() }

    override fun onAvatarUpdated(data: Any) {
        when(data) {
            is Bitmap -> binding.user.imgAvatar.setImageBitmap(data)
            is Uri -> Glide.with(requireContext()).load(data).into(binding.user.imgAvatar)
        }
    }

    override fun onNameUpdated(data: String) {
        binding.user.tvName.text = data
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}