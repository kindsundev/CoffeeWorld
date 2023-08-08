package kind.sun.dev.coffeeworld.ui.more.user.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.databinding.FragmentProfileBinding
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.common.Logger
import kind.sun.dev.coffeeworld.utils.view.LoadingDialog
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
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
        binding.fragment = this
        profileViewModel.getUser()
        setupUserLiveData()
    }

    private fun setupUserLiveData() {
        profileViewModel.userResponseLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkResult.Success -> {
                    loadingDialog.dismiss()
                    val user = it.data!!.data
                    binding.user.apply {
                        userModel = user
                        fragment = this@ProfileFragment
                    }
                }
                is NetworkResult.Error -> {
                    loadingDialog.dismiss()
                    Logger.error(it.message.toString())
                }
                is NetworkResult.Loading -> {
                    loadingDialog.show(parentFragmentManager, LoadingDialog::class.simpleName)
                }
            }
        }
    }

    fun onShowAvatarFragment() {
        findNavController().navigate(R.id.action_profileFragment_to_avatarFragment)
    }
    fun onBackToMoreFragment() { findNavController().popBackStack() }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}