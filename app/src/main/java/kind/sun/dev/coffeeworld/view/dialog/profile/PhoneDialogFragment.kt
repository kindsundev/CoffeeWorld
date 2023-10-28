package kind.sun.dev.coffeeworld.view.dialog.profile

import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseDialog
import kind.sun.dev.coffeeworld.databinding.DialogUpdatePhoneBinding
import kind.sun.dev.coffeeworld.utils.helper.animation.setScaleAnimation
import kind.sun.dev.coffeeworld.utils.helper.view.showErrorMessage
import kind.sun.dev.coffeeworld.utils.helper.view.showToast
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
                requireContext().showToast(it.data).also {
                    onUpdateSuccess()
                    onCancel()
                }
            },
            onError = { requireContext().showToast(it) }
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