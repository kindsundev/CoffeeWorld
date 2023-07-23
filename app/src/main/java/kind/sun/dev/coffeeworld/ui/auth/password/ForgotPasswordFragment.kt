package kind.sun.dev.coffeeworld.ui.auth.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.databinding.FragmentForgotPasswordBinding
import kind.sun.dev.coffeeworld.ui.auth.login.LoginFragment
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.view.LoadingDialog
import javax.inject.Inject

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var loadingDialog: LoadingDialog
    private val passwordViewModel by viewModels<ForgotPasswordViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(layoutInflater)
        initDataBinding()
        return binding.root
    }

    private fun initDataBinding() {
        binding.fragment = this
        binding.viewModel = passwordViewModel
        binding.lifecycleOwner = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupErrorValidationObserver()
        setupPasswordResetResponseObserver()
    }

    private fun setupPasswordResetResponseObserver() {
        passwordViewModel.passwordResetResponseLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkResult.Success -> {
                    loadingDialog.dismiss()
                    binding.tvResponse.text = it.data!!.data
                }
                is NetworkResult.Error -> {
                    loadingDialog.dismiss()
                    binding.tvResponse.text = it.message
                }
                is NetworkResult.Loading -> {
                    loadingDialog.show(parentFragmentManager, LoadingDialog::class.simpleName)
                }
            }
        }
    }

    private fun setupErrorValidationObserver() {
        passwordViewModel.errorMessageLiveData.observe(viewLifecycleOwner) {
            binding.tvResponse.text = it
        }
    }

    fun backToLoginFragment() {
        val registerFragment = LoginFragment()
        requireActivity().supportFragmentManager.commit {
            setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
            replace(R.id.fragment_container, registerFragment)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}