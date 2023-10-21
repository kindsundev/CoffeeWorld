package kind.sun.dev.coffeeworld.base

import androidx.lifecycle.ViewModel
import kind.sun.dev.coffeeworld.utils.helper.network.NetworkHelper
import javax.inject.Inject

open class BaseViewModel : ViewModel() {

     @Inject
     lateinit var networkHelper: NetworkHelper

     protected fun handleCheckAndRoute(
          conditionChecker: (() -> Pair<Boolean, String>)?,
          onPassedCheck: () -> Unit,
          onFailedCheck: (reason: String, localDataRequired: Boolean) -> Unit
     ) {
          if (networkHelper.isConnected) {
               conditionChecker?.let {
                    if (it().first) {
                         onPassedCheck()
                    } else {
                         onFailedCheck(it().second, false)
                    }
               } ?: onPassedCheck()
          } else {
               onFailedCheck("Please check your internet", true)
          }
     }

}