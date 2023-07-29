package kind.sun.dev.coffeeworld.ui.cafe

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.data.model.response.cafe.CafeModel
import kind.sun.dev.coffeeworld.databinding.FragmentDialogCafeDetailBottomSheetBinding
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.common.getSerializableSafe

@AndroidEntryPoint
class CafeDetailBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding : FragmentDialogCafeDetailBottomSheetBinding? = null
    private val binding get() = _binding!!
    private var cafeModel: CafeModel? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setOnShowListener { it ->
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout = bottomSheetDialog.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            )
            parentLayout?.let { view ->
                val layoutParams = view.layoutParams.apply {
                    val screenHeight = Resources.getSystem().displayMetrics.heightPixels
                    height = screenHeight
                }
                view.layoutParams = layoutParams

                val behavior = BottomSheetBehavior.from(view)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogCafeDetailBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cafeModel = arguments?.getSerializableSafe(Constants.CAFE_KEY)
        initDataBinding()
        initCafeInfo()
    }

    private fun initDataBinding(){
        cafeModel?.let {
            binding.apply {
                cafeModel = it
                fragment = this@CafeDetailBottomSheetFragment
            }
        }
    }

    private fun initCafeInfo() {
        cafeModel?.let {
            val locationShort = it.location.split(",")[0]
            binding.tvLocationOverview.text = locationShort
        }
    }

    fun exitBottomSheet() {
        this.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}