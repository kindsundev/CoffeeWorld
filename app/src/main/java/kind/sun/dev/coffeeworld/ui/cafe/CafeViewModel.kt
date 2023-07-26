package kind.sun.dev.coffeeworld.ui.cafe

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.data.repository.CafeRepository
import kind.sun.dev.coffeeworld.utils.network.NetworkHelper
import javax.inject.Inject

@HiltViewModel
class CafeViewModel @Inject constructor(
    private val cafeRepository: CafeRepository,
    private val networkHelper: NetworkHelper
): ViewModel() {

}