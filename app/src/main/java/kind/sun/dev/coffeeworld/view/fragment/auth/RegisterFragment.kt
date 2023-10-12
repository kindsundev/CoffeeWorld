package kind.sun.dev.coffeeworld.view.fragment.auth

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseFragment
import kind.sun.dev.coffeeworld.databinding.FragmentRegisterBinding
import kind.sun.dev.coffeeworld.viewmodel.AuthViewModel
import kind.sun.dev.coffeeworld.utils.helper.animation.setScaleAnimation
import kind.sun.dev.coffeeworld.utils.common.Constants

@AndroidEntryPoint
class RegisterFragment: BaseFragment<FragmentRegisterBinding, AuthViewModel>(
    FragmentRegisterBinding::inflate
){
    override val viewModel: AuthViewModel by viewModels()

    override fun setupDataBinding() {
        binding.apply {
            fragment = this@RegisterFragment
            authViewModel = viewModel
            lifecycleOwner = this@RegisterFragment
        }
    }
    override fun initViews() {}

    override fun observeViewModel() {
        observeValidatorError(viewModel.errorMessage) {
            binding.tvResponse.apply {
                visibility = View.VISIBLE
                text = it
            }
        }

        observeNetworkResult(viewModel.registerResponse,
            onSuccess = {
                Toast.makeText(activity, it.data, Toast.LENGTH_LONG).show()
                popFragment()
            },
            onError = {
                binding.tvResponse.apply {
                    visibility = View.VISIBLE
                    text = it
                }
            }
        )
    }

    fun onCLickRegister(view: View) {
        view.setScaleAnimation(Constants.DURATION_SHORT, Constants.SCALE_LOW) {
            viewModel.onRegister()
        }
    }

    fun backToLoginFragment(view: View) {
        view.setScaleAnimation(Constants.DURATION_SHORT, Constants.SCALE_LOW) {
            popFragment()
        }
    }
}