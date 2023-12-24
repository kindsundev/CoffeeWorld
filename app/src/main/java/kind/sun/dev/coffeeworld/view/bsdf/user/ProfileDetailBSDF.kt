package kind.sun.dev.coffeeworld.view.bsdf.user

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseBSDF
import kind.sun.dev.coffeeworld.base.BaseViewModel
import kind.sun.dev.coffeeworld.data.local.model.UserModel
import kind.sun.dev.coffeeworld.databinding.BsdfProfileDetailBinding
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.helper.view.getParcelableHelper

@AndroidEntryPoint
class ProfileDetailBSDF : BaseBSDF<BsdfProfileDetailBinding, BaseViewModel>(
    isFullScreen = false, BsdfProfileDetailBinding::inflate
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