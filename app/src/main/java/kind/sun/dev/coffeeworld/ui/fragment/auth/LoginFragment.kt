package kind.sun.dev.coffeeworld.ui.fragment.auth

import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.base.BaseFragment
import kind.sun.dev.coffeeworld.databinding.FragmentLoginBinding
import kind.sun.dev.coffeeworld.util.helper.animation.setScaleAnimation
import kind.sun.dev.coffeeworld.util.helper.view.showErrorMessage
import kind.sun.dev.coffeeworld.viewmodel.AuthViewModel

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, AuthViewModel>(
    layoutInflater = FragmentLoginBinding::inflate,
    viewModelClass = AuthViewModel::class.java
) {
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
        viewModel!!.loginResponse.observeNetworkResult(
            onSuccess = {
                preferences.userToken = it.data.token
                navigateToFragment(R.id.action_loginFragment_to_homeFragment)
            },
            onError = {
                binding.tvError.showErrorMessage(it)
            }
        )
    }

    fun onClickRegister(view: View) {
        view.setScaleAnimation { navigateToFragment(R.id.action_loginFragment_to_registerFragment) }
    }

    fun onClickForgotPassword(view: View) {
        view.setScaleAnimation { navigateToFragment(R.id.action_loginFragment_to_forgotPasswordFragment) }
    }

    fun onCLickLogin(view: View) {
        view.setScaleAnimation {
            viewModel?.onLogin {
                binding.tvError.showErrorMessage(it)
            }
        }
    }
}