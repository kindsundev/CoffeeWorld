package kind.sun.dev.coffeeworld.view.fragment.more.settings

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseFragment
import kind.sun.dev.coffeeworld.base.BaseViewModel
import kind.sun.dev.coffeeworld.databinding.FragmentSettingsBinding

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding, BaseViewModel>(
    FragmentSettingsBinding::inflate
) {

    override val viewModel: BaseViewModel by viewModels()

    override fun setupDataBinding() {

    }

    override fun initViews() {

    }

    override fun observeViewModel() {

    }
}