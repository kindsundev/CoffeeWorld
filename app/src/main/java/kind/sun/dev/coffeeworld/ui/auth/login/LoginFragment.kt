package kind.sun.dev.coffeeworld.ui.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.databinding.FragmentLoginBinding
import kind.sun.dev.coffeeworld.ui.MainActivity
import kind.sun.dev.coffeeworld.ui.auth.password.ForgotPasswordFragment
import kind.sun.dev.coffeeworld.ui.auth.register.RegisterFragment
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.api.TokenManager
import kind.sun.dev.coffeeworld.utils.view.LoadingDialog
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var loadingDialog: LoadingDialog

    private val loginViewModel by viewModels<LoginViewModel>()
    @Inject lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        initDataBinding()
        checkTokenAndRedirect()
        return binding.root
    }

    private fun initDataBinding() {
        binding.fragment = this
        binding.viewModel = loginViewModel
        binding.lifecycleOwner = this
    }

    private fun checkTokenAndRedirect() {
        if(tokenManager.getToken() != null) { startMainActivity() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupErrorValidationObserver()
        setupLoginResponseObserver()
    }

    private fun setupErrorValidationObserver() {
        loginViewModel.errorMessageLiveData.observe(viewLifecycleOwner) {
            binding.tvError.text = it
        }
    }

    private fun setupLoginResponseObserver() {
        loginViewModel.loginResponseLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkResult.Success -> {
                    loadingDialog.dismiss()
                    tokenManager.saveToken(it.data!!.data.token)
                    startMainActivity()
                }
                is NetworkResult.Error -> {
                    loadingDialog.dismiss()
                    binding.tvError.text = it.message
                }
                is NetworkResult.Loading -> {
                    loadingDialog.show(childFragmentManager, LoadingDialog::class.simpleName)
                }
            }
        }
    }

    fun onClickRegister() {
        val registerFragment = RegisterFragment()
        requireActivity().supportFragmentManager.commit {
            setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
            addToBackStack(null)
            replace(R.id.fragment_container, registerFragment)
        }
    }

    fun onClickForgotPassword() {
        val passwordFragment = ForgotPasswordFragment()
        requireActivity().supportFragmentManager.commit {
            setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
            addToBackStack(null)
            replace(R.id.fragment_container, passwordFragment)
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(requireContext(), MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK )
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}