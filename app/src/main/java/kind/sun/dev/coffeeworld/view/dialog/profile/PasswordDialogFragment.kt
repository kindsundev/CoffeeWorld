package kind.sun.dev.coffeeworld.view.dialog.profile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.databinding.DialogUpdatePasswordBinding
import kind.sun.dev.coffeeworld.viewmodel.ProfileViewModel
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.custom.CustomLoadingDialog
import javax.inject.Inject

@AndroidEntryPoint
class PasswordDialogFragment : DialogFragment() {
    private var _binding: DialogUpdatePasswordBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel by viewModels<ProfileViewModel>()
    @Inject
    lateinit var loadingDialog: CustomLoadingDialog
    val isPasswordVisible = MutableLiveData<Boolean>(false)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogUpdatePasswordBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(
            requireActivity(), R.style.dialog_material).apply {
            setCancelable(false)
            setView(binding.root)
        }.create()
        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.CENTER)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setupDataBinding()
        setupErrorValidationObserver()
        setupUserUpdateObserver()
        return binding.root
    }

    private fun setupDataBinding() {
        binding.fragment = this
        binding.viewModel = profileViewModel
    }

    private fun setupErrorValidationObserver() {
        profileViewModel.validator.observe(viewLifecycleOwner) {
            binding.tvError.apply {
                visibility = View.VISIBLE
                text = it
            }
        }
    }

    private fun setupUserUpdateObserver() {
        profileViewModel.messageResponse.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkResult.Success -> {
                    if (loadingDialog.isAdded) {
                        loadingDialog.dismiss()
                        Toast.makeText(requireContext(), it.data!!.data, Toast.LENGTH_SHORT).show()
                        onCancel()
                    }
                }
                is NetworkResult.Error -> {
                    if (loadingDialog.isAdded) {
                        loadingDialog.dismiss()
                        binding.tvError.text = it.message
                    }
                }
                is NetworkResult.Loading -> {
                    loadingDialog.show(childFragmentManager, CustomLoadingDialog::class.simpleName)
                }
            }
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}