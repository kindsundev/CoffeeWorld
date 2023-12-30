package kind.sun.dev.coffeeworld.ui.dialog.profile

import android.text.InputType
import android.view.View
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseDF
import kind.sun.dev.coffeeworld.databinding.DfProfileUpdateEmailBinding
import kind.sun.dev.coffeeworld.util.helper.animation.setScaleAnimation
import kind.sun.dev.coffeeworld.util.helper.view.showErrorMessage
import kind.sun.dev.coffeeworld.util.helper.view.showToastError
import kind.sun.dev.coffeeworld.viewmodel.UserViewModel

@AndroidEntryPoint
class ProfileUpdateEmailDF(
    private val onUpdateSuccess: (message: String) -> Unit
) : BaseDF<DfProfileUpdateEmailBinding, UserViewModel>(
    bindingInflater = DfProfileUpdateEmailBinding::inflate,
    viewModelClass = UserViewModel::class.java
) {

    val isPasswordVisible = MutableLiveData(false)

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
            viewModel?.onUpdateEmail {
                binding.tvError.showErrorMessage(it)
            }
        }
    }
}