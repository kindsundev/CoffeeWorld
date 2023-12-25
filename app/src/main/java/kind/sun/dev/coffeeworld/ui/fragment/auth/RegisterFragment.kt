package kind.sun.dev.coffeeworld.ui.fragment.auth

import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseFragment
import kind.sun.dev.coffeeworld.databinding.FragmentRegisterBinding
import kind.sun.dev.coffeeworld.viewmodel.AuthViewModel
import kind.sun.dev.coffeeworld.util.helper.animation.setScaleAnimation
import kind.sun.dev.coffeeworld.util.helper.view.showErrorMessage
import kind.sun.dev.coffeeworld.util.helper.view.showToastSuccess

@AndroidEntryPoint
class RegisterFragment: BaseFragment<FragmentRegisterBinding, AuthViewModel>(
    layoutInflater = FragmentRegisterBinding::inflate,
    viewModelClass = AuthViewModel::class.java
){
    override fun setupDataBinding() {
        binding.apply {
            fragment = this@RegisterFragment
            authViewModel = viewModel
            lifecycleOwner = this@RegisterFragment
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

    fun onCLickRegister(view: View) {
        view.setScaleAnimation {
            viewModel?.onRegister {
                binding.tvResponse.showErrorMessage(it)
            }
        }
    }

    fun backToLoginFragment(view: View) {
        view.setScaleAnimation { popFragment() }
    }
}