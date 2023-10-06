package kind.sun.dev.coffeeworld.utils.custom

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.databinding.CustomEditTextViewBinding
import kind.sun.dev.coffeeworld.utils.helper.view.dpToPx
import kind.sun.dev.coffeeworld.utils.helper.view.toEditable

class CustomEditTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private val binding by lazy {
        CustomEditTextViewBinding.inflate(LayoutInflater.from(context), this, true)
    }
    private var textHint: String? = null
    private var textValue: String? = null
    private var icon: Drawable? = null
    private var background: Drawable? = null
    private var editTextType: String? = null
    private var cardViewRadius: Float? = null
    private var cardViewElevation: Int? = null

    init {
        context.obtainStyledAttributes(attrs, R.styleable.CustomEditTextView, 0, 0)
            .let {
                textHint = it.getString(R.styleable.CustomEditTextView_etv_text_hint)
                textValue = it.getString(R.styleable.CustomEditTextView_etv_text)
                icon = it.getDrawable(R.styleable.CustomEditTextView_etv_icon_custom_src)
                background = it.getDrawable(R.styleable.CustomEditTextView_etv_background_custom_src)
                editTextType = it.getString(R.styleable.CustomEditTextView_etv_input_type)
                cardViewRadius = it.getFloat(R.styleable.CustomEditTextView_etv_card_view_corner_radius, -1f)
                cardViewElevation = it.getInt(R.styleable.CustomEditTextView_etv_card_view_elevation, -1)
                it.recycle()
            }.also {
                initCardView()
                initEditText()
            }
    }

    /*
    * Set a shadow for the card view,
    * the value should be equal to the border of the background to make it more beautiful
    * */
    private fun initCardView() = binding.cardView.apply {
        if (cardViewElevation != -1 && cardViewElevation != null) elevation = cardElevation
        if (cardViewRadius != -1f && cardViewRadius != null)  radius = cardViewRadius as Float
    }

    private fun initEditText() = binding.editText.apply {
        compoundDrawablePadding = (16.dpToPx(context))
        textHint?.let { hint = it }
        textValue?.let { text = it.toEditable() }
        background?.let { background = it }
        inputType = when(editTextType) {
            "text" -> InputType.TYPE_CLASS_TEXT
            "phone" -> InputType.TYPE_CLASS_PHONE
            "number" -> InputType.TYPE_CLASS_NUMBER
            "email" -> InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            "name" -> InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            "password" -> InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            else -> InputType.TYPE_CLASS_TEXT
        }
        icon?.let {
            setCompoundDrawablesRelativeWithIntrinsicBounds(it, null, null, null)
        }
    }

}