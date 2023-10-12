package kind.sun.dev.coffeeworld.view.fragment.home

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseFragment
import kind.sun.dev.coffeeworld.databinding.FragmentHomeBinding
import kind.sun.dev.coffeeworld.viewmodel.CafeViewModel

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, CafeViewModel>(
    FragmentHomeBinding::inflate
) {

    override val viewModel: CafeViewModel by viewModels()

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
                    if (doubleBackPressed) {
                        requireActivity().finish()
                    } else {
                        doubleBackPressed = true
                        Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
                        Handler(Looper.getMainLooper()).postDelayed({ doubleBackPressed = false }, 2000)
                    }
                }
            }
        )
    }

    override fun initViews() {

    }

    override fun observeViewModel() {

    }
}