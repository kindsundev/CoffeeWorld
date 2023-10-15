package kind.sun.dev.coffeeworld.view.fragment.order

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseFragment
import kind.sun.dev.coffeeworld.databinding.FragmentOrderBinding
import kind.sun.dev.coffeeworld.viewmodel.OrderViewModel

@AndroidEntryPoint
class OrderFragment : BaseFragment<FragmentOrderBinding, OrderViewModel>(
    FragmentOrderBinding::inflate
) {
    override val viewModel: OrderViewModel by viewModels()

    // Todo => Asynchronously call multiple tasks in parallel

    override fun initAnything() {
        requireActivity().onBackPressedDispatcher.addCallback(
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    popToHomeFragment()
                }
            }
        )
    }

    override fun setupDataBinding() { }

    override fun prepareData() {

    }

    override fun initViews() {

    }

    override fun observeViewModel() {

    }
}