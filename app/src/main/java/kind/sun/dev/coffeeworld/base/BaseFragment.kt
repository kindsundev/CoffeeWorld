package kind.sun.dev.coffeeworld.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.contract.FragmentContract
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.custom.CustomLoadingDialog
import kind.sun.dev.coffeeworld.utils.helper.storage.PreferencesHelper
import kind.sun.dev.coffeeworld.view.MainActivity
import javax.inject.Inject

abstract class BaseFragment<V : ViewDataBinding, VM: BaseViewModel>(
    private val bindingInflater: (inflater: LayoutInflater) -> V
) : Fragment(), FragmentContract {

    private var _binding: V? = null
    protected val binding: V get() = _binding as V
    protected abstract val viewModel: VM

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
        _binding = bindingInflater.invoke(inflater)
        setupDataBinding()
        prepareData()
        return binding.root
    }

    open fun prepareData() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAnything()
        initViews()
        observeViewModel()
    }

    open fun initAnything() {}

    override fun onDestroyView() {
        super.onDestroyView()
        cleanUp()
        _binding = null
    }

    open fun cleanUp() {}

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

    protected fun navigateToFragment(resId: Int, bundle: Bundle? = null) {
        navController.apply {
            bundle?.let { navigate(resId, it) } ?: navigate(resId)
        }
    }

    protected fun popToHomeFragment() { navController.popBackStack(R.id.homeFragment, false) }

    protected fun popFragment() { navController.popBackStack() }

    protected fun getBottomNavigationHeight() = mainActivity.getBottomNavigationHeight()

}