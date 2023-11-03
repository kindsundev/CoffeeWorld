package kind.sun.dev.coffeeworld.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.base.BaseViewModel
import kind.sun.dev.coffeeworld.contract.CafeContract
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val cafeService: CafeContract.Service
): BaseViewModel(){


}