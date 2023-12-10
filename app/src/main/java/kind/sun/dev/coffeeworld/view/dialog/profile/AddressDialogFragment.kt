package kind.sun.dev.coffeeworld.view.dialog.profile

import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.base.BaseDialog
import kind.sun.dev.coffeeworld.databinding.DialogUpdateAddressBinding
import kind.sun.dev.coffeeworld.utils.helper.animation.setScaleAnimation
import kind.sun.dev.coffeeworld.utils.helper.view.showErrorMessage
import kind.sun.dev.coffeeworld.utils.helper.view.showToastError
import kind.sun.dev.coffeeworld.viewmodel.UserViewModel

@AndroidEntryPoint
class AddressDialogFragment(
    private val onUpdateSuccess: (message: String) -> Unit
) : BaseDialog<DialogUpdateAddressBinding, UserViewModel>(DialogUpdateAddressBinding::inflate) {

    override val viewModel: UserViewModel by viewModels()

    override fun setupDataBinding() {
        binding.apply {
            fragment = this@AddressDialogFragment
            profileViewModel = viewModel
        }
    }

    override fun initViews() {}

    override fun observeViewModel() {
        viewModel.messageResponse.observeNetworkResult(
            onSuccess = {
                onUpdateSuccess(it.data)
                onCancel()
            },
            onError = { requireContext().showToastError(it) }
        )
    }

    fun onClickUpdateAddress(view: View) {
        view.setScaleAnimation {
            viewModel.onUpdateAddress {
                binding.tvError.showErrorMessage(it)
            }
        }
    }

    fun onCancel() : Unit = this.dismiss()
}