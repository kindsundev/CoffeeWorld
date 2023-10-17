package kind.sun.dev.coffeeworld.view.fragment.auth

import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.base.BaseFragment
import kind.sun.dev.coffeeworld.databinding.FragmentLoginBinding
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.helper.animation.setScaleAnimation
import kind.sun.dev.coffeeworld.viewmodel.AuthViewModel

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, AuthViewModel>(
    FragmentLoginBinding::inflate
) {
    override val viewModel: AuthViewModel by viewModels()

    override fun setupDataBinding() {
        binding.apply {
            fragment = this@LoginFragment
            authViewModel = viewModel
            lifecycleOwner = this@LoginFragment
        }
    }

    override fun prepareData() {
        preferences.userToken?.let {
            navigateToFragment(R.id.action_loginFragment_to_homeFragment)
        }
    }

    override fun initViews() {}

    override fun observeViewModel() {
        observeValidatorError(viewModel.validator) {
            binding.tvError.apply {
                visibility = View.VISIBLE
                text = it
            }
        }

        observeNetworkResult(viewModel.loginResponse,
            onSuccess = {
                preferences.userToken = it.data.token
                navigateToFragment(R.id.action_loginFragment_to_homeFragment)
            },
            onError = {
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = it
            }
        )
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
            viewModel.onLogin()
        }
    }
}