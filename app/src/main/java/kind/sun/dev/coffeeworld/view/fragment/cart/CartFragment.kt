package kind.sun.dev.coffeeworld.view.fragment.cart

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseFragment
import kind.sun.dev.coffeeworld.base.BaseViewModel
import kind.sun.dev.coffeeworld.databinding.FragmentCartBinding

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding, BaseViewModel>(
    FragmentCartBinding::inflate
){
    override val viewModel: BaseViewModel by viewModels()

    override fun initAnything() {
        requireActivity().onBackPressedDispatcher.addCallback(
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    popToHomeFragment()
                }
            })
    }

    override fun setupDataBinding() {

    }

    override fun initViews() {

    }

    override fun observeViewModel() {

    }
}