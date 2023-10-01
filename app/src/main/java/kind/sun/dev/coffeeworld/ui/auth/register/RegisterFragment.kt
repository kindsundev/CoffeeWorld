package kind.sun.dev.coffeeworld.ui.auth.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.databinding.FragmentRegisterBinding
import kind.sun.dev.coffeeworld.ui.auth.AuthViewModel
import kind.sun.dev.coffeeworld.utils.animation.setScaleAnimation
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.view.LoadingDialog
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var loadingDialog: LoadingDialog
    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        initDataBinding()
        return binding.root
    }

    private fun initDataBinding() {
        binding.apply {
            fragment = this@RegisterFragment
            lifecycleOwner = this@RegisterFragment
            viewModel = authViewModel
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupErrorValidationObserver()
        setupRegisterResponseObserver()
    }

    private fun setupErrorValidationObserver() {
        authViewModel.errorMessage.observe(viewLifecycleOwner) {
            binding.tvResponse.text = it
        }
    }

    private fun setupRegisterResponseObserver() {
        authViewModel.registerResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    if (loadingDialog.isAdded) {
                        loadingDialog.dismiss()
                        Toast.makeText(activity, it.data!!.data, Toast.LENGTH_LONG).show()
                        findNavController().popBackStack()
                    }
                }
                is NetworkResult.Error -> {
                    if (loadingDialog.isAdded) {
                        loadingDialog.dismiss()
                        binding.tvResponse.text = it.message
                    }
                }
                is NetworkResult.Loading -> {
                    loadingDialog.show(childFragmentManager, LoadingDialog::class.simpleName)
                }
            }
        }
    }

    fun onCLickRegister(view: View) {
        view.setScaleAnimation(Constants.DURATION_SHORT, Constants.SCALE_LOW) {
            authViewModel.onRegister()
        }
    }

    fun backToLoginFragment(view: View) {
        view.setScaleAnimation(Constants.DURATION_SHORT, Constants.SCALE_LOW) {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}