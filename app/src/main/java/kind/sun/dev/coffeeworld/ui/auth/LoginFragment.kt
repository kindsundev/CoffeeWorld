package kind.sun.dev.coffeeworld.ui.auth

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
import kind.sun.dev.coffeeworld.data.model.request.auth.LoginRequest
import kind.sun.dev.coffeeworld.databinding.FragmentLoginBinding
import kind.sun.dev.coffeeworld.ui.MainActivity
import kind.sun.dev.coffeeworld.utils.Logger
import kind.sun.dev.coffeeworld.utils.NetworkResult
import kind.sun.dev.coffeeworld.utils.TokenManager
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel>()
    @Inject lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.loginFragment = this
        binding.lifecycleOwner = this
        checkTokenAndRedirect()
        return binding.root
    }

    private fun checkTokenAndRedirect() {
        if(tokenManager.getToken() != null) {
          startActivity(Intent(requireContext(), MainActivity::class.java))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        authViewModel.authLoginResponseLiveData.observe(viewLifecycleOwner) {
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

    fun login() {
        val validationResult = validLoginDataInput()
        if(validationResult.first) {
            authViewModel.loginUser(getLoginRequest())
        } else {
            binding.tvError.text = validationResult.second
        }
    }

    private fun validLoginDataInput(): Pair<Boolean, String> {
        val loginRequest = getLoginRequest()
        return authViewModel.validateLoginCredentials(loginRequest)
    }

    private fun getLoginRequest(): LoginRequest {
        val username = binding.edtUsername.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        return LoginRequest(username, password)
    }

    fun navigateToRegister() {
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