package kind.sun.dev.coffeeworld.base

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.custom.CustomLoadingDialog
import kind.sun.dev.coffeeworld.utils.helper.storage.PreferencesHelper
import kind.sun.dev.coffeeworld.utils.helper.view.monitorNetworkOperation
import javax.inject.Inject

abstract class BaseBSDF<V: ViewDataBinding, VM: BaseViewModel>(
    private val layoutInflater: (inflater: LayoutInflater) -> V,
    private val viewModelClass: Class<VM>?,
    private val isFullScreen: Boolean
) : BottomSheetDialogFragment() {

    private var _binding: V? = null
    protected val binding: V get() = _binding as V

    protected val viewModel : VM? by lazy {
        viewModelClass?.let {
            ViewModelProvider(this)[it]
        }
    }

    @Inject lateinit var preferences: PreferencesHelper
    @Inject lateinit var loadingDialog: CustomLoadingDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (isFullScreen) {
            return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
                setOnShowListener {listener ->
                    (listener as BottomSheetDialog).findViewById<View>(
                        com.google.android.material.R.id.design_bottom_sheet
                    )?.let { view ->
                        view.layoutParams.height = Resources.getSystem().displayMetrics.heightPixels
                        BottomSheetBehavior.from(view).state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = layoutInflater.invoke(inflater)
        setupDataBinding()
        prepareData()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        if (viewModel != null) observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cleanUp()
        _binding = null
    }

    abstract fun initViews()
    open fun setupDataBinding() {}
    open fun prepareData() {}
    open fun initAnything() {}
    open fun cleanUp() {}
    open fun observeViewModel() {}
    protected fun exit() : Unit = this.dismiss()

    protected fun <T> LiveData<NetworkResult<T>>.observeNetworkResult(
        onSuccess: (T) -> Unit,
        onError: (String) -> Unit
    ) {
        monitorNetworkOperation(viewLifecycleOwner, childFragmentManager, loadingDialog,
            onSuccess = {
                onSuccess.invoke(it)
            }, onError = {
                onError.invoke(it)
            }
        )
    }
}