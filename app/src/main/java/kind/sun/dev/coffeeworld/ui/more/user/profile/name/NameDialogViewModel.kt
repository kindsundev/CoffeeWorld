package kind.sun.dev.coffeeworld.ui.more.user.profile.name

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kind.sun.dev.coffeeworld.data.repository.UserRepository
import kind.sun.dev.coffeeworld.utils.network.NetworkHelper
import javax.inject.Inject

@HiltViewModel
class NameDialogViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val networkHelper: NetworkHelper
): ViewModel() {

}