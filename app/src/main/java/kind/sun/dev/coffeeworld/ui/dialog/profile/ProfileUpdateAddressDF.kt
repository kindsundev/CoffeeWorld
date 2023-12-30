package kind.sun.dev.coffeeworld.ui.dialog.profile

import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseDF
import kind.sun.dev.coffeeworld.databinding.DfProfileUpdateAddressBinding
import kind.sun.dev.coffeeworld.util.helper.animation.setScaleAnimation
import kind.sun.dev.coffeeworld.util.helper.view.showErrorMessage
import kind.sun.dev.coffeeworld.util.helper.view.showToastError
import kind.sun.dev.coffeeworld.viewmodel.UserViewModel

@AndroidEntryPoint
class ProfileUpdateAddressDF(
    private val onUpdateSuccess: (message: String) -> Unit
) : BaseDF<DfProfileUpdateAddressBinding, UserViewModel>(
    bindingInflater = DfProfileUpdateAddressBinding::inflate,
    viewModelClass = UserViewModel::class.java
) {

    override fun setupDataBinding() {
        binding.apply {
            fragment = this@ProfileUpdateAddressDF
            profileViewModel = viewModel
        }
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

    fun onClickUpdateAddress(view: View) {
        view.setScaleAnimation {
            viewModel?.onUpdateAddress {
                binding.tvError.showErrorMessage(it)
            }
        }
    }

    fun onCancel(): Unit = this.dismiss()
}