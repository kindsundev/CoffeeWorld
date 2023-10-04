package kind.sun.dev.coffeeworld.view.fragment.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.databinding.FragmentLoginBinding
import kind.sun.dev.coffeeworld.viewmodel.AuthViewModel
import kind.sun.dev.coffeeworld.utils.helper.animation.setScaleAnimation
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.helper.storage.TokenPreferencesHelper
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.custom.CustomLoadingDialog
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var loadingDialog: CustomLoadingDialog

    private val authViewModel by viewModels<AuthViewModel>()
    @Inject lateinit var tokenManager: TokenPreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        initDataBinding()
        checkTokenAndRedirect()
        return binding.root
    }

    private fun initDataBinding() {
        binding.apply {
            fragment = this@LoginFragment
            viewModel = authViewModel
            lifecycleOwner = this@LoginFragment
        }
    }

    private fun checkTokenAndRedirect() {
        if(tokenManager.getToken() != null) {
            navigateToFragment(R.id.action_loginFragment_to_homeFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupErrorValidationObserver()
        setupLoginResponseObserver()
    }

    private fun setupErrorValidationObserver() {
        authViewModel.errorMessage.observe(viewLifecycleOwner) {
            binding.tvError.text = it
        }
    }

    private fun setupLoginResponseObserver() {
        authViewModel.loginResponse.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkResult.Success -> {
                    if (loadingDialog.isAdded) {
                        loadingDialog.dismiss()
                        tokenManager.saveToken(it.data!!.data.token)
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }
                }
                is NetworkResult.Error -> {
                    if (loadingDialog.isAdded) {
                        loadingDialog.dismiss()
                        binding.tvError.text = it.message
                    }
                }
                is NetworkResult.Loading -> {
                    loadingDialog.show(childFragmentManager, CustomLoadingDialog::class.simpleName)
                }
            }
        }
    }

    fun onClickRegister(view: View) {
        view.setScaleAnimation(Constants.DURATION_SHORT, Constants.SCALE_LOW) {
            navigateToFragment(R.id.action_loginFragment_to_registerFragment)
        }

    }

    fun onClickForgotPassword(view: View) {
        view.setScaleAnimation(Constants.DURATION_SHORT, Constants.SCALE_LOW) {
            navigateToFragment(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
    }

    fun onCLickLogin(view: View) {
        view.setScaleAnimation(Constants.DURATION_SHORT, Constants.SCALE_LOW) {
            authViewModel.onLogin()
        }
    }

    private fun navigateToFragment(resId: Int) : Unit = findNavController().navigate(resId)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}