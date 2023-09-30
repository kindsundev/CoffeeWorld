package kind.sun.dev.coffeeworld.ui.more.user.profile.phone

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.databinding.DialogUpdatePhoneBinding
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.view.LoadingDialog
import javax.inject.Inject

@AndroidEntryPoint

class PhoneDialogFragment(
    private val onUpdateSuccess: () -> Unit
) : DialogFragment() {
    private var _binding: DialogUpdatePhoneBinding? = null
    private val binding get() = _binding!!
    private val phoneViewModel by viewModels<PhoneDialogViewModel>()
    @Inject lateinit var loadingDialog: LoadingDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogUpdatePhoneBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(
            requireContext(), R.style.dialog_material).apply {
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
        binding.apply {
            fragment = this@PhoneDialogFragment
            viewModel = phoneViewModel
        }
    }

    private fun setupErrorValidationObserver() {
        phoneViewModel.errorMessageLiveData.observe(viewLifecycleOwner) {
            binding.tvError.apply {
                visibility = View.VISIBLE
                text = it
            }
        }
    }

    private fun setupUserUpdateObserver() {
        phoneViewModel.userUpdate.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkResult.Success -> {
                    if (loadingDialog.isAdded) {
                        loadingDialog.dismiss()
                        Toast.makeText(requireContext(), it.data!!.data, Toast.LENGTH_SHORT).show()
                        onCancel()
                        onUpdateSuccess.invoke()
                    }
                }
                is NetworkResult.Error -> {
                    if (loadingDialog.isAdded) {
                        loadingDialog.dismiss()
                        binding.tvError.text = it.message
                    }
                }
                is NetworkResult.Loading -> {
                    loadingDialog.show(childFragmentManager, LoadingDialog::class.simpleName)
                }
            }
        }
    }

    fun onCancel() : Unit = this.dismiss()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}