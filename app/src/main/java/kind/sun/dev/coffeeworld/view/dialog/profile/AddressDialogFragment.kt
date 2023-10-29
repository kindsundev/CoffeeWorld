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
import kind.sun.dev.coffeeworld.viewmodel.ProfileViewModel

@AndroidEntryPoint
class AddressDialogFragment(
    private val onUpdateSuccess: () -> Unit
) : BaseDialog<DialogUpdateAddressBinding, ProfileViewModel>(DialogUpdateAddressBinding::inflate) {

    override val viewModel: ProfileViewModel by viewModels()

    override fun setupDataBinding() {
        binding.apply {
            fragment = this@AddressDialogFragment
            profileViewModel = viewModel
        }
    }

    override fun initViews() {}

    override fun observeViewModel() {
        observeNetworkResult(
            liveData = viewModel.messageResponse,
            onSuccess = {
                StyleableToast.makeText(requireContext(), it.data, R.style.toast_success).show().also {
                    onUpdateSuccess()
                    onCancel()
                }
            },
            onError = {
                StyleableToast.makeText(requireContext(), it, R.style.toast_error).show()
            }
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