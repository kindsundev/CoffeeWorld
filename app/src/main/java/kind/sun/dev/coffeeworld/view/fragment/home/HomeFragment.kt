package kind.sun.dev.coffeeworld.view.fragment.home

import androidx.activity.OnBackPressedCallback
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.base.BaseFragment
import kind.sun.dev.coffeeworld.base.BaseViewModel
import kind.sun.dev.coffeeworld.databinding.FragmentHomeBinding
import kind.sun.dev.coffeeworld.utils.helper.view.showSnackbarMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, BaseViewModel>(
    FragmentHomeBinding::inflate
) {

    override val viewModel: BaseViewModel by viewModels()

    private var doubleBackPressed = false

    override fun setupDataBinding() {

    }

    override fun prepareData() {
        // todo-task: create base view model
    }

    override fun initAnything() {
        requireActivity().onBackPressedDispatcher.addCallback(
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    checkDoubleBackPressed()
                }
            }
        )
    }

    private fun checkDoubleBackPressed() {
        if (doubleBackPressed) requireActivity().finish()

        showSnackbarMessage(
            context = requireContext(),
            root = binding.root as CoordinatorLayout,
            resMessageId = R.string.press_back_again
        ).also {
            lifecycleScope.launch {
                doubleBackPressed = true
                delay(2000)
                doubleBackPressed = false
            }
        }
    }


    override fun initViews() {

    }

    override fun observeViewModel() {

    }
}