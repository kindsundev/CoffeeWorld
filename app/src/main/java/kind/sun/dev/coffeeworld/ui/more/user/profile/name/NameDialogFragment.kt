package kind.sun.dev.coffeeworld.ui.more.user.profile.name

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
import kind.sun.dev.coffeeworld.databinding.DialogUpdateNameBinding
import kind.sun.dev.coffeeworld.ui.more.user.profile.ProfileUpdateCallback
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.view.LoadingDialog
import javax.inject.Inject

@AndroidEntryPoint
class NameDialogFragment(
    private val listener: ProfileUpdateCallback
) : DialogFragment() {
    private var _binding: DialogUpdateNameBinding? = null
    private val binding get() = _binding!!

    private val nameViewModel by viewModels<NameDialogViewModel>()
    @Inject lateinit var loadingDialog: LoadingDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogUpdateNameBinding.inflate(layoutInflater)
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
        binding.viewModel = nameViewModel
    }

    private fun setupErrorValidationObserver() {
        nameViewModel.errorMessageLiveData.observe(viewLifecycleOwner) {
            binding.tvError.apply {
                visibility = View.VISIBLE
                text = it
            }
        }
    }

    private fun setupUserUpdateObserver() {
        nameViewModel.userUpdateResponseLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkResult.Success -> {
                    loadingDialog.dismiss()
                    setupNameObserver()
                    Toast.makeText(requireContext(), it.data!!.data, Toast.LENGTH_SHORT).show()
                    onCancel()
                }
                is NetworkResult.Error -> {
                    loadingDialog.dismiss()
                    binding.tvError.text = it.message
                }
                is NetworkResult.Loading -> {
                    loadingDialog.show(childFragmentManager, LoadingDialog::class.simpleName)
                }
            }
        }
    }

    private fun setupNameObserver() {
        nameViewModel.nameLiveData.observe(viewLifecycleOwner) {
            listener.onNameUpdated(it)
        }
    }

    fun onCancel() : Unit = this.dismiss()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}