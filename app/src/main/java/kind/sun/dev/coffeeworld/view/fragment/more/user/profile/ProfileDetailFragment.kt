package kind.sun.dev.coffeeworld.view.fragment.more.user.profile

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseBottomSheet
import kind.sun.dev.coffeeworld.base.BaseViewModel
import kind.sun.dev.coffeeworld.data.local.model.UserModel
import kind.sun.dev.coffeeworld.databinding.FragmentProfileDetailBinding
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.helper.view.getParcelableHelper

@AndroidEntryPoint
class ProfileDetailFragment : BaseBottomSheet<FragmentProfileDetailBinding, BaseViewModel>(
    isFullScreen = false, FragmentProfileDetailBinding::inflate
){

    override val viewModel: BaseViewModel  by viewModels()

    private var userModel: UserModel? = null

    override fun setupDataBinding() {}


    override fun prepareData() {
        userModel = arguments?.getParcelableHelper(Constants.USER_KEY)
    }

    override fun initViews() {
        userModel?.let { binding.user = it }
    }

    override fun observeViewModel() {}
}