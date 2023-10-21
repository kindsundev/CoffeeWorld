package kind.sun.dev.coffeeworld.view.fragment.auth

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseFragment
import kind.sun.dev.coffeeworld.databinding.FragmentForgotPasswordBinding
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.helper.animation.setScaleAnimation
import kind.sun.dev.coffeeworld.utils.helper.view.showErrorMessage
import kind.sun.dev.coffeeworld.viewmodel.AuthViewModel

@AndroidEntryPoint
class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding, AuthViewModel>(
    FragmentForgotPasswordBinding::inflate
) {
    override val viewModel: AuthViewModel by viewModels()

    override fun setupDataBinding() {
        binding.apply {
            fragment = this@ForgotPasswordFragment
            lifecycleOwner = this@ForgotPasswordFragment
            authViewModel = viewModel
        }
    }

    override fun initViews() {}

    override fun observeViewModel() {
        observeNetworkResult(viewModel.messageResponse,
            onSuccess = {
                Toast.makeText(activity, it.data, Toast.LENGTH_LONG).show()
                popFragment()
            },
            onError = {
                binding.tvResponse.showErrorMessage(it)
            }
        )
    }

    fun onCLickForgotPassword(view: View) {
        view.setScaleAnimation(Constants.DURATION_SHORT, Constants.SCALE_LOW) {
            viewModel.onPasswordReset {
                binding.tvResponse.showErrorMessage(it)
            }
        }
    }

    fun backToLoginFragment(view: View) {
        view.setScaleAnimation(Constants.DURATION_SHORT, Constants.SCALE_LOW) {
            popFragment()
        }
    }
}