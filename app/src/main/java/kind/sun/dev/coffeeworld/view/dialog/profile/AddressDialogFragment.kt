package kind.sun.dev.coffeeworld.view.dialog.profile

import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseDialog
import kind.sun.dev.coffeeworld.databinding.DialogUpdateAddressBinding
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.helper.animation.setScaleAnimation
import kind.sun.dev.coffeeworld.utils.helper.view.showErrorMessage
import kind.sun.dev.coffeeworld.utils.helper.view.showToast
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
                requireContext().showToast(it.data).also {
                    onUpdateSuccess()
                    onCancel()
                }
            },
            onError = { requireContext().showToast(it) })
    }


    fun onClickUpdateAddress(view: View) {
        view.setScaleAnimation(Constants.DURATION_SHORT, Constants.SCALE_LOW) {
            viewModel.onUpdateAddress {
                binding.tvError.showErrorMessage(it)
            }
        }
    }

    fun onCancel() : Unit = this.dismiss()
}