package kind.sun.dev.coffeeworld.view.dialog.profile

import android.text.InputType
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.base.BaseDialog
import kind.sun.dev.coffeeworld.databinding.DialogUpdateEmailBinding
import kind.sun.dev.coffeeworld.utils.helper.animation.setScaleAnimation
import kind.sun.dev.coffeeworld.utils.helper.view.showErrorMessage
import kind.sun.dev.coffeeworld.viewmodel.ProfileViewModel

@AndroidEntryPoint
class EmailDialogFragment(
    private val onUpdateSuccess: () -> Unit
) : BaseDialog<DialogUpdateEmailBinding, ProfileViewModel>(DialogUpdateEmailBinding::inflate) {

    override val viewModel by viewModels<ProfileViewModel>()
    val isPasswordVisible = MutableLiveData<Boolean>(false)

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

    fun onCancel() : Unit = this.dismiss()

    fun onShowPasswordChecked(isChecked: Boolean) {
        binding.edtPassword.apply {
            inputType = if (isChecked) {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            text?.let { setSelection(it.length) }
        }
    }

    fun onClickUpdateEmail(view: View) {
        view.setScaleAnimation {
            viewModel.onUpdateEmail {
                binding.tvError.showErrorMessage(it)
            }
        }
    }
}