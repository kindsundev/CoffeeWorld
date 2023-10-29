package kind.sun.dev.coffeeworld.view.dialog.profile

import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.base.BaseDialog
import kind.sun.dev.coffeeworld.databinding.DialogUpdatePhoneBinding
import kind.sun.dev.coffeeworld.utils.helper.animation.setScaleAnimation
import kind.sun.dev.coffeeworld.utils.helper.view.showErrorMessage
import kind.sun.dev.coffeeworld.viewmodel.ProfileViewModel

@AndroidEntryPoint

class PhoneDialogFragment(
    private val onUpdateSuccess: () -> Unit
) : BaseDialog<DialogUpdatePhoneBinding, ProfileViewModel>(DialogUpdatePhoneBinding::inflate) {

    override val viewModel by viewModels<ProfileViewModel>()

    override fun setupDataBinding() {
        binding.fragment = this
        binding.profileViewModel = viewModel
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

    fun onClickUpdatePhone(view: View) {
        view.setScaleAnimation {
            viewModel.onUpdatePhone {
                binding.tvError.showErrorMessage(it)
            }
        }
    }

    fun onCancel() : Unit = this.dismiss()
}