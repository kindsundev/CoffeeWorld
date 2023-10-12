package kind.sun.dev.coffeeworld.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.custom.CustomLoadingDialog
import javax.inject.Inject

abstract class BaseFragment<VDB : ViewDataBinding, VM: ViewModel>(
    private val bindingInflater: (inflater: LayoutInflater) -> VDB
) : Fragment() {

    private var _binding: VDB? = null
    protected val binding: VDB get() = _binding as VDB

    protected abstract val viewModel: VM
    @Inject lateinit var loadingDialog: CustomLoadingDialog

    private var doubleBackPressed = false

    protected val onDoubleBackExit = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (doubleBackPressed) {
                requireActivity().finish()
            } else {
                doubleBackPressed = true
                Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    doubleBackPressed = false
                }, 2000)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = bindingInflater.invoke(inflater)
        setupDataBinding()
        prepareData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAnything()
        initViews()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun setupDataBinding()

    abstract fun prepareData()

    open fun initAnything() {}

    abstract fun initViews()

    abstract fun observeViewModel()

    protected fun <T> observeNetworkResult(
        liveData: LiveData<NetworkResult<T>>,
        onSuccess: (T) -> Unit,
        onError: (String) -> Unit
    ) {
        liveData.observe(viewLifecycleOwner) { result ->
            when(result) {
                is NetworkResult.Success -> {
                    if (loadingDialog.isAdded) loadingDialog.dismiss()
                    result.data?.let { onSuccess.invoke(it) }
                }
                is NetworkResult.Error -> {
                    if (loadingDialog.isAdded) loadingDialog.dismiss()
                    result.message?.let { onError.invoke(it) }
                }
                is NetworkResult.Loading -> {
                    loadingDialog.show(childFragmentManager, this.tag)
                }
            }
        }
    }

    protected fun observeValidatorError(
        liveData: MutableLiveData<String>, onMessage: (String) -> Unit
    ): Unit = liveData.observe(viewLifecycleOwner) { onMessage.invoke(it) }

    protected fun navigateToFragment(resId: Int, bundle: Bundle? = null) {
        findNavController().apply {
            bundle?.let { navigate(resId, it) } ?: navigate(resId)
        }
    }

}