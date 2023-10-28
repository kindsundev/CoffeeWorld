package kind.sun.dev.coffeeworld.view.fragment.auth

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseFragment
import kind.sun.dev.coffeeworld.databinding.FragmentRegisterBinding
import kind.sun.dev.coffeeworld.viewmodel.AuthViewModel
import kind.sun.dev.coffeeworld.utils.helper.animation.setScaleAnimation
import kind.sun.dev.coffeeworld.utils.helper.view.showErrorMessage

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

    fun onCLickRegister(view: View) {
        view.setScaleAnimation {
            viewModel.onRegister {
                binding.tvResponse.showErrorMessage(it)
            }
        }
    }

    fun backToLoginFragment(view: View) {
        view.setScaleAnimation { popFragment() }
    }
}