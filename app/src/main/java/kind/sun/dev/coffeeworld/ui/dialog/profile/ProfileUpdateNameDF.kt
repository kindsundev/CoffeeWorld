package kind.sun.dev.coffeeworld.ui.dialog.profile

import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseDF
import kind.sun.dev.coffeeworld.databinding.DfProfileUpdateNameBinding
import kind.sun.dev.coffeeworld.util.helper.animation.setScaleAnimation
import kind.sun.dev.coffeeworld.util.helper.view.showErrorMessage
import kind.sun.dev.coffeeworld.util.helper.view.showToastError
import kind.sun.dev.coffeeworld.viewmodel.UserViewModel

@AndroidEntryPoint
class ProfileUpdateNameDF(
    private val onUpdateSuccess: (message: String) -> Unit
) : BaseDF<DfProfileUpdateNameBinding, UserViewModel>(
    bindingInflater = DfProfileUpdateNameBinding::inflate,
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

    fun onCancel(): Unit = this.dismiss()

    fun onClickUpdateName(view: View) {
        view.setScaleAnimation {
            viewModel?.onUpdateName {
                binding.tvError.showErrorMessage(it)
            }
        }
    }
}