package kind.sun.dev.coffeeworld.ui.more.user.profile.name

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.databinding.DialogUpdateNameBinding
import kind.sun.dev.coffeeworld.ui.more.user.profile.ProfileUpdateCallback

@AndroidEntryPoint
class NameDialogFragment(
    private val listener: ProfileUpdateCallback
) : DialogFragment() {
    private var _binding: DialogUpdateNameBinding? = null
    private val binding get() = _binding!!

    private val nameViewModel by viewModels<NameDialogViewModel>()

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
        binding.fragment = this
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    fun onCancel() : Unit = this.dismiss()


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}