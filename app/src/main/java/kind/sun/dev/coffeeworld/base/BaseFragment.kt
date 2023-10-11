package kind.sun.dev.coffeeworld.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<VDB : ViewDataBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> VDB
) : Fragment() {

    private var _binding: VDB? = null

    protected val binding: VDB get() = _binding as VDB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = bindingInflater.invoke(inflater)
        setupDataBinding()
        initialize()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun setupDataBinding()

    abstract fun initialize()

    abstract fun initViews()

    abstract fun observeViewModel()



}