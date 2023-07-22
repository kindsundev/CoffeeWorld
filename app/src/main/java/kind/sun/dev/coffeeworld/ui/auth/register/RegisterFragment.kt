package kind.sun.dev.coffeeworld.ui.auth.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.databinding.FragmentRegisterBinding
import kind.sun.dev.coffeeworld.ui.auth.login.LoginFragment

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        binding.registerFragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun backToLoginFragment() {
        val registerFragment = LoginFragment()
        requireActivity().supportFragmentManager.commit {
            setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
            replace(R.id.fragment_container, registerFragment)
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}