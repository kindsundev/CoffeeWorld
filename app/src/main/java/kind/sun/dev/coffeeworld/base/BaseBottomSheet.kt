package kind.sun.dev.coffeeworld.base

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kind.sun.dev.coffeeworld.contract.FragmentContract

abstract class BaseBottomSheet<V: ViewDataBinding>(
    private val isFullScreen: Boolean,
    private val bindingInflater: (inflater: LayoutInflater) -> V
) : BottomSheetDialogFragment(), FragmentContract {
    private var _binding: V? = null
    protected val binding: V get() = _binding as V

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
        _binding = bindingInflater.invoke(inflater)
        setupDataBinding()
        prepareData()
        return binding.root
    }

    open fun prepareData() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()
    }

    protected fun exit() : Unit = this.dismiss()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}