package kind.sun.dev.coffeeworld.ui.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.databinding.FragmentLoginBinding
import kind.sun.dev.coffeeworld.ui.MainActivity
import kind.sun.dev.coffeeworld.ui.auth.register.RegisterFragment
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.api.TokenManager
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel by viewModels<LoginViewModel>()
    @Inject lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        initDataBinding()
        checkTokenAndRedirect()
        return binding.root
    }

    private fun initDataBinding() {
        binding.fragment = this
        binding.viewModel = loginViewModel
        binding.lifecycleOwner = this
    }

    private fun checkTokenAndRedirect() {
        if(tokenManager.getToken() != null) {
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupErrorValidationObserver()
        setupLoginResponseObserver()
    }

    private fun setupErrorValidationObserver() {
        loginViewModel.errorMessageLiveData.observe(viewLifecycleOwner) {
            binding.tvError.text = it
        }
    }

    private fun setupLoginResponseObserver() {
        loginViewModel.loginResponseLiveData.observe(viewLifecycleOwner) {
            // hide progress bar
            when(it) {
                is NetworkResult.Success -> {
//                    tokenManager.saveToken(it.data!!.data.token)
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                }
                is NetworkResult.Error -> {
                    binding.tvError.text = it.message
                }
                is NetworkResult.Loading -> {
                    // show progress bar
                }
            }
        }
    }

    fun onClickRegister() {
        val registerFragment = RegisterFragment()
        requireActivity().supportFragmentManager.commit {
            addToBackStack(null)
            replace(R.id.fragment_container, registerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}