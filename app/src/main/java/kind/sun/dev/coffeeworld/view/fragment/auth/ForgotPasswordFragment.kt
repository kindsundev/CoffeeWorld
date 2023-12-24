package kind.sun.dev.coffeeworld.view.fragment.auth

import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseFragment
import kind.sun.dev.coffeeworld.databinding.FragmentForgotPasswordBinding
import kind.sun.dev.coffeeworld.utils.helper.animation.setScaleAnimation
import kind.sun.dev.coffeeworld.utils.helper.view.showErrorMessage
import kind.sun.dev.coffeeworld.utils.helper.view.showToastSuccess
import kind.sun.dev.coffeeworld.viewmodel.AuthViewModel

@AndroidEntryPoint
class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding, AuthViewModel>(
    layoutInflater =  FragmentForgotPasswordBinding::inflate,
    viewModelClass = AuthViewModel::class.java
) {
    override fun setupDataBinding() {
        binding.apply {
            fragment = this@ForgotPasswordFragment
            lifecycleOwner = this@ForgotPasswordFragment
            authViewModel = viewModel
        }
    }

    override fun initViews() {}

    override fun observeViewModel() {
        viewModel!!.messageResponse.observeNetworkResult(
            onSuccess = {
                requireContext().showToastSuccess(it.data)
                popFragment()
            },
            onError = {
                binding.tvResponse.showErrorMessage(it)
            }
        )
    }

    fun onCLickForgotPassword(view: View) {
        view.setScaleAnimation {
            viewModel?.onPasswordReset {
                binding.tvResponse.showErrorMessage(it)
            }
        }
    }

    fun backToLoginFragment(view: View) {
        view.setScaleAnimation { popFragment() }
    }
}