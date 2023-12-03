package kind.sun.dev.coffeeworld.view.dialog.profile

import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.base.BaseDialog
import kind.sun.dev.coffeeworld.databinding.DialogUpdateNameBinding
import kind.sun.dev.coffeeworld.utils.common.Logger
import kind.sun.dev.coffeeworld.utils.helper.animation.setScaleAnimation
import kind.sun.dev.coffeeworld.utils.helper.view.showErrorMessage
import kind.sun.dev.coffeeworld.viewmodel.UserViewModel

@AndroidEntryPoint
class NameDialogFragment(
    private val onUpdateSuccess: (message: String) -> Unit
) : BaseDialog<DialogUpdateNameBinding, UserViewModel>(DialogUpdateNameBinding::inflate){

    override val viewModel by viewModels<UserViewModel>()

    override fun setupDataBinding() {
        binding.fragment = this
        binding.profileViewModel = viewModel
    }

    override fun initViews() {}

    override fun observeViewModel() {
        viewModel.messageResponse.observeNetworkResult(
            onSuccess = {
                Logger.error("called")
                onUpdateSuccess(it.data)
                onCancel()
            },
            onError = {
                StyleableToast.makeText(requireContext(), it, R.style.toast_error).show()
            }
        )
    }

    fun onCancel() : Unit = this.dismiss()

    fun onClickUpdateName(view: View) {
        view.setScaleAnimation {
            viewModel.onUpdateName {
                binding.tvError.showErrorMessage(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}