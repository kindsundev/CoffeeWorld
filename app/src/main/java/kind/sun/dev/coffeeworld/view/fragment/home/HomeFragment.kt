package kind.sun.dev.coffeeworld.view.fragment.home

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


    override fun setupDataBinding() {

    }

    override fun prepareData() {
        viewModel.getCafeList()
    }

    override fun initAnything() {
        requireActivity().onBackPressedDispatcher.addCallback(onDoubleBackExit)
    }

    override fun initViews() {

    }

    override fun observeViewModel() {
        handleNetworkResult(viewModel.cafe,
            onSuccess = {

            }, onError = {

            })
    }


}