package kind.sun.dev.coffeeworld.ui.bsdf.user

import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseBSDF
import kind.sun.dev.coffeeworld.data.local.model.UserModel
import kind.sun.dev.coffeeworld.databinding.BsdfProfileDetailBinding
import kind.sun.dev.coffeeworld.util.common.Constants
import kind.sun.dev.coffeeworld.util.helper.view.getParcelableHelper

@AndroidEntryPoint
class ProfileDetailBSDF : BaseBSDF<BsdfProfileDetailBinding, Nothing>(
    layoutInflater = BsdfProfileDetailBinding::inflate,
    viewModelClass = null,
    isFullScreen = false,
){
    private var userModel: UserModel? = null

    override fun prepareData() {
        userModel = arguments?.getParcelableHelper(Constants.USER_KEY)
    }

    override fun initViews() {
        userModel?.let { binding.user = it }
    }
}