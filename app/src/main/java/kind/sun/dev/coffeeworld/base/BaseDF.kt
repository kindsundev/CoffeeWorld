package kind.sun.dev.coffeeworld.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.contract.FragmentContract
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.custom.CustomLoadingDialog
import kind.sun.dev.coffeeworld.utils.helper.view.monitorNetworkOperationOnce
import javax.inject.Inject

abstract class BaseDF<V: ViewDataBinding, VM: BaseViewModel>(
    private val bindingInflater: (inflater: LayoutInflater) -> V
) : DialogFragment(), FragmentContract {

    private var _binding: V? = null
    protected val binding: V get() = _binding as V

    protected abstract val viewModel: VM
    @Inject lateinit var loadingDialog: CustomLoadingDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = bindingInflater.invoke(layoutInflater)
        return MaterialAlertDialogBuilder(
            requireContext(), R.style.dialog_material
        ).apply {
            setCancelable(false)
            setView(binding.root)
        }.create().also { dialog ->
            dialog.window?.apply {
                setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                setGravity(Gravity.CENTER)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setupDataBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()
    }

    protected fun <T> LiveData<NetworkResult<T>>.observeNetworkResult(
        onSuccess: (T) -> Unit,
        onError: (String) -> Unit
    ) {
        monitorNetworkOperationOnce(this@BaseDF, childFragmentManager, loadingDialog,
            onSuccess = {
                onSuccess.invoke(it)
            }, onError = {
                onError.invoke(it)
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}