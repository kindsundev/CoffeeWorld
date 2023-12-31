package kind.sun.dev.coffeeworld.util.helper.view

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kind.sun.dev.coffeeworld.util.api.NetworkResult
import kind.sun.dev.coffeeworld.util.custom.CustomLoadingDialog

fun <T> LiveData<NetworkResult<T>>.monitorNetworkOperation(
    lifecycleOwner: LifecycleOwner,
    fragmentManager: FragmentManager,
    loadingDialog: CustomLoadingDialog,
    onSuccess: (T) -> Unit,
    onError: (String) -> Unit
) {
    observe(lifecycleOwner) { result ->
        handleNetworkResult(result, fragmentManager, loadingDialog, onSuccess, onError)
    }
}

fun <T> LiveData<NetworkResult<T>>.monitorNetworkOperationOnce(
    lifecycleOwner: LifecycleOwner,
    fragmentManager: FragmentManager,
    loadingDialog: CustomLoadingDialog,
    onSuccess: (T) -> Unit,
    onError: (String) -> Unit
) {
    observerOnce(lifecycleOwner) { result ->
        handleNetworkResult(result, fragmentManager, loadingDialog, onSuccess, onError)
    }
}

private fun <T> LiveData<T>.observerOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    var firstObservation = true
    observe(lifecycleOwner) { value ->
        if (firstObservation) {
            firstObservation = false
        } else {
            observer.onChanged(value)
            removeObserver(observer)
        }
    }
}

private fun <T> handleNetworkResult(
    result: NetworkResult<T>,
    fragmentManager: FragmentManager,
    loadingDialog: CustomLoadingDialog,
    onSuccess: (T) -> Unit,
    onError: (String) -> Unit
) {
    when(result) {
        is NetworkResult.Success -> {
            loadingDialog.checkToDismiss()
            result.data?.let { onSuccess.invoke(it) }
        }
        is NetworkResult.Error -> {
            loadingDialog.checkToDismiss()
            result.message?.let { onError.invoke(it) }
        }
        is NetworkResult.Loading -> {
            loadingDialog.checkToDismiss()
            loadingDialog.show(fragmentManager, null)
        }
    }
}
