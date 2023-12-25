package kind.sun.dev.coffeeworld.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.util.api.NetworkResult
import kind.sun.dev.coffeeworld.util.custom.CustomLoadingDialog
import kind.sun.dev.coffeeworld.util.helper.storage.PreferencesHelper
import kind.sun.dev.coffeeworld.util.helper.view.monitorNetworkOperation
import kind.sun.dev.coffeeworld.ui.MainActivity
import javax.inject.Inject

abstract class BaseFragment<VB : ViewDataBinding, VM: BaseViewModel>(
    private val layoutInflater: (inflater: LayoutInflater) -> VB,
    private val viewModelClass: Class<VM>?,
) : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB get() = _binding as VB

    protected val viewModel : VM? by lazy {
        viewModelClass?.let {
            ViewModelProvider(this)[it]
        }
    }

    private lateinit var mainActivity: MainActivity
    private val navController by lazy { findNavController() }
    @Inject lateinit var preferences: PreferencesHelper
    @Inject lateinit var loadingDialog: CustomLoadingDialog

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mainActivity = context
        } else {
            throw IllegalStateException("Context mus be an instance of ${MainActivity::class.simpleName}")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = layoutInflater.invoke(inflater)
        setupDataBinding()
        prepareData()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAnything()
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
    open fun observeViewModel() { }

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

    protected fun navigateToFragment(resId: Int, bundle: Bundle? = null) {
        navController.apply {
            bundle?.let { navigate(resId, it) } ?: navigate(resId)
        }
    }

    protected fun popToHomeFragment() { navController.popBackStack(R.id.homeFragment, false) }

    protected fun popFragment() { navController.popBackStack() }

    protected fun getBottomNavigationHeight() = mainActivity.getBottomNavigationHeight()

}