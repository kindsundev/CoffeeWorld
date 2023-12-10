package kind.sun.dev.coffeeworld.view.dialog.profile

import android.text.InputType
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.base.BaseDialog
import kind.sun.dev.coffeeworld.databinding.DialogUpdatePasswordBinding
import kind.sun.dev.coffeeworld.utils.helper.animation.setScaleAnimation
import kind.sun.dev.coffeeworld.utils.helper.view.showErrorMessage
import kind.sun.dev.coffeeworld.utils.helper.view.showToastError
import kind.sun.dev.coffeeworld.utils.helper.view.showToastSuccess
import kind.sun.dev.coffeeworld.viewmodel.UserViewModel

@AndroidEntryPoint
class PasswordDialogFragment : BaseDialog<DialogUpdatePasswordBinding, UserViewModel>(
    DialogUpdatePasswordBinding::inflate
){
    override val viewModel by viewModels<UserViewModel>()
    val isPasswordVisible = MutableLiveData<Boolean>(false)

    override fun setupDataBinding() {
        binding.fragment = this
        binding.profileViewModel = viewModel
    }

    override fun initViews() {}

    override fun observeViewModel() {
        viewModel.messageResponse.observeNetworkResult(
            onSuccess = {
                requireContext().showToastSuccess(it.data)
                onCancel()
            },
            onError = { requireContext().showToastError(it) }
        )
    }

    fun onCancel() : Unit = this.dismiss()

    fun onShowPasswordChecked(isChecked: Boolean) {
        val newInputType = if (isChecked) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        binding.apply {
            edtCurrentPassword.apply {
                inputType = newInputType
                text?.let { setSelection(it.length) }
            }
            edtNewPassword.apply {
                inputType = newInputType
                text?.let { setSelection(it.length) }
            }
            edtReNewPassword.apply {
                inputType = newInputType
                text?.let { setSelection(it.length) }
            }
        }
    }

    fun onClickUpdatePassword(view: View) {
        view.setScaleAnimation {
            viewModel.onUpdatePassword {
                binding.tvError.showErrorMessage(it)
            }
        }
    }
}