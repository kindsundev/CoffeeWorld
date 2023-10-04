package kind.sun.dev.coffeeworld.view.fragment.more.user.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.data.model.response.auth.UserModel
import kind.sun.dev.coffeeworld.databinding.FragmentProfileDetailBinding
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.helper.view.getParcelableSafe

@AndroidEntryPoint
class ProfileDetailBottomFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentProfileDetailBinding? = null
    private val binding get() = _binding!!
    private var userModel: UserModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userModel = arguments?.getParcelableSafe(Constants.USER_KEY)
        userModel?.let {
            binding.user = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}