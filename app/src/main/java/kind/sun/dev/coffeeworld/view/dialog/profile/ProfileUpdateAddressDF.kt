package kind.sun.dev.coffeeworld.view.dialog.profile

import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseDialogFragment
import kind.sun.dev.coffeeworld.databinding.DfProfileUpdateAddressBinding
import kind.sun.dev.coffeeworld.utils.helper.animation.setScaleAnimation
import kind.sun.dev.coffeeworld.utils.helper.view.showErrorMessage
import kind.sun.dev.coffeeworld.utils.helper.view.showToastError
import kind.sun.dev.coffeeworld.viewmodel.UserViewModel

@AndroidEntryPoint
class ProfileUpdateAddressDF(
    private val onUpdateSuccess: (message: String) -> Unit
) : BaseDialogFragment<DfProfileUpdateAddressBinding, UserViewModel>(
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