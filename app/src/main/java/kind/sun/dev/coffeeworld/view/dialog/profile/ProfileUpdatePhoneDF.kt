package kind.sun.dev.coffeeworld.view.dialog.profile

import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseDialogFragment
import kind.sun.dev.coffeeworld.databinding.DfProfileUpdatePhoneBinding
import kind.sun.dev.coffeeworld.utils.helper.animation.setScaleAnimation
import kind.sun.dev.coffeeworld.utils.helper.view.showErrorMessage
import kind.sun.dev.coffeeworld.utils.helper.view.showToastError
import kind.sun.dev.coffeeworld.viewmodel.UserViewModel

@AndroidEntryPoint
class ProfileUpdatePhoneDF(
    private val onUpdateSuccess: (message: String) -> Unit
) : BaseDialogFragment<DfProfileUpdatePhoneBinding, UserViewModel>(
    bindingInflater = DfProfileUpdatePhoneBinding::inflate,
    viewModelClass = UserViewModel::class.java
) {
    override fun setupDataBinding() {
        binding.fragment = this
        binding.profileViewModel = viewModel
    }


    override fun observeViewModel() {
        viewModel!!.messageResponse.observeNetworkResult(
            onSuccess = {
                onUpdateSuccess(it.data)
                onCancel()
            },
            onError = { requireContext().showToastError(it) }
        )
    }

    fun onClickUpdatePhone(view: View) {
        view.setScaleAnimation {
            viewModel?.onUpdatePhone {
                binding.tvError.showErrorMessage(it)
            }
        }
    }

    fun onCancel() : Unit = this.dismiss()
}