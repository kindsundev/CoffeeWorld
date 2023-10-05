package kind.sun.dev.coffeeworld.utils.custom

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.databinding.CustomInputCardViewBinding
import kind.sun.dev.coffeeworld.utils.helper.view.toEditable

class CustomInputCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private val binding by lazy {
        CustomInputCardViewBinding.inflate(LayoutInflater.from(context), this, true)
    }
    private var textHint: String? = null
    private var textValue: String? = null
    private var imgResource: Drawable? = null
    private var edtInputType: String? = null

    init {
        context.obtainStyledAttributes(attrs, R.styleable.CustomInputCardView, 0, 0)
            .let {
                textHint = it.getString(R.styleable.CustomInputCardView_icv_text_hint)
                textValue = it.getString(R.styleable.CustomInputCardView_icv_text)
                imgResource = it.getDrawable(R.styleable.CustomInputCardView_icv_img_src)
                edtInputType = it.getString(R.styleable.CustomInputCardView_icv_input_type)
                it.recycle()
            }.also {
                bindData()
            }
    }

    private fun bindData() {
        binding.apply {
            imgIcon.apply {
                imgResource?.let { setImageDrawable(it)}
            }
            edtInput.apply {
                textHint?.let { hint = it }
                textValue?.let { text = it.toEditable() }
                inputType = when(edtInputType) {
                    "text" -> InputType.TYPE_CLASS_TEXT
                    "phone" -> InputType.TYPE_CLASS_PHONE
                    "number" -> InputType.TYPE_CLASS_NUMBER
                    "email" -> InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    "name" -> InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                    "password" -> InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
                    else -> InputType.TYPE_CLASS_TEXT
                }
            }
        }
    }
}