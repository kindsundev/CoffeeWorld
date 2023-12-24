package kind.sun.dev.coffeeworld.view.fragment.more.user.payment

import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.base.BaseFragment
import kind.sun.dev.coffeeworld.databinding.FragmentPaymentBinding

@AndroidEntryPoint
class PaymentFragment  : BaseFragment<FragmentPaymentBinding, Nothing>(
    layoutInflater = FragmentPaymentBinding::inflate,
    viewModelClass = null
) {
    override fun initViews() {

    }
}