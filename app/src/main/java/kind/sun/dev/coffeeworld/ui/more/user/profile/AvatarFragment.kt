package kind.sun.dev.coffeeworld.ui.more.user.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.databinding.FragmentAvatarBinding

@AndroidEntryPoint
class AvatarFragment: BottomSheetDialogFragment() {
    private var _binding: FragmentAvatarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAvatarBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragment = this
    }

    fun onClickOpenGallery() {
        Toast.makeText(requireContext(), "Gallery is open", Toast.LENGTH_SHORT).show()
    }

    fun onClickOpenCamera() {
        Toast.makeText(requireContext(), "Camera is open", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}