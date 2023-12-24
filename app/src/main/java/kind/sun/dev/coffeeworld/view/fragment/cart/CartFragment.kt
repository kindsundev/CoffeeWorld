package kind.sun.dev.coffeeworld.view.fragment.cart

import androidx.activity.OnBackPressedCallback
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseFragment
import kind.sun.dev.coffeeworld.databinding.FragmentCartBinding

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding, Nothing>(
    layoutInflater = FragmentCartBinding::inflate,
    viewModelClass = null
){

    override fun initAnything() {
        requireActivity().onBackPressedDispatcher.addCallback(
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    popToHomeFragment()
                }
            })
    }

    override fun initViews() {

    }
}