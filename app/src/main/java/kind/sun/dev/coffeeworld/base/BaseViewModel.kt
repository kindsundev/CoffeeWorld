package kind.sun.dev.coffeeworld.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.helper.network.NetworkHelper
import javax.inject.Inject

open class BaseViewModel : ViewModel() {

     @Inject
     lateinit var networkHelper: NetworkHelper

     protected val error by lazy { MutableLiveData<String>() }

     protected fun checkThenExecute(
          validator: (() -> Pair<Boolean, String>)?,
          onAllowed: () -> Unit
     ) {
          if (networkHelper.isConnected) {
               validator?.let {
                    if (it().first) {
                         onAllowed.invoke()
                    } else {
                         error.value = it().second
                    }
               } ?: onAllowed.invoke()
          } else {
               error.value = Constants.NO_INTERNET_CONNECTION
          }
     }

}