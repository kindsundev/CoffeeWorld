package kind.sun.dev.coffeeworld.ui.auth.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.databinding.FragmentRegisterBinding
import kind.sun.dev.coffeeworld.ui.auth.login.LoginFragment
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.view.LoadingDialog
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var loadingDialog: LoadingDialog
    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        initDataBinding()
        return binding.root
    }

    private fun initDataBinding() {
        binding.fragment = this
        binding.viewModel = registerViewModel
        binding.lifecycleOwner = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupErrorValidationObserver()
        setupRegisterResponseObserver()
    }

    private fun setupErrorValidationObserver() {
        registerViewModel.errorMessageLiveData.observe(viewLifecycleOwner) {
            binding.tvResponse.text = it
        }
    }

    private fun setupRegisterResponseObserver() {
        registerViewModel.registerResponseLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkResult.Success -> {
                    loadingDialog.dismiss()
                    binding.tvResponse.text = it.data!!.data
                    backToLoginFragment()
                }
                is NetworkResult.Error -> {
                    loadingDialog.dismiss()
                    binding.tvResponse.text = it.message
                }
                is NetworkResult.Loading -> {
                    loadingDialog.show(parentFragmentManager, LoadingDialog::class.simpleName)
                }
            }
        }
    }

    fun backToLoginFragment() {
        val registerFragment = LoginFragment()
        requireActivity().supportFragmentManager.commit {
            setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
            addToBackStack(null)
            replace(R.id.fragment_container, registerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}