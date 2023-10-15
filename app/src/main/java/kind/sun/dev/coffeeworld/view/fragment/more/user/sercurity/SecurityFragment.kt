package kind.sun.dev.coffeeworld.view.fragment.more.user.sercurity

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseFragment
import kind.sun.dev.coffeeworld.base.BaseViewModel
import kind.sun.dev.coffeeworld.databinding.FragmentSecurityBinding

@AndroidEntryPoint
class SecurityFragment : BaseFragment<FragmentSecurityBinding, BaseViewModel>(
    FragmentSecurityBinding::inflate
) {
    override val viewModel: BaseViewModel by viewModels()

    override fun setupDataBinding() {

    }

    override fun initViews() {

    }

    override fun observeViewModel() {

    }
}