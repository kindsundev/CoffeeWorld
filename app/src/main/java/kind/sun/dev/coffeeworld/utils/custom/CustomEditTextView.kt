package kind.sun.dev.coffeeworld.utils.custom

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.adapters.ListenerUtil
import androidx.lifecycle.MutableLiveData
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.databinding.CustomEditTextViewBinding
import kind.sun.dev.coffeeworld.utils.helper.view.dpToPx

class CustomEditTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr){

    private val binding by lazy {
        CustomEditTextViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var textHint: String? = null
    private var iconCustom: Drawable? = null
    private var backgroundCustom: Drawable? = null
    private var editTextType: String? = null
    private var editText: EditText = binding.editText

    private val hidePasswordDrawable by lazy { getDrawableResource(R.drawable.ic_baseline_visibility_off_24) }
    private val showPasswordDrawable by lazy { getDrawableResource(R.drawable.ic_baseline_visibility_24) }
    private var isClickHandled = false

    private fun getDrawableResource(resId: Int): Drawable? = AppCompatResources.getDrawable(context, resId)

    init {
        context.obtainStyledAttributes(attrs, R.styleable.CustomEditTextView, 0, 0)
            .let {
                textHint = it.getString(R.styleable.CustomEditTextView_etv_text_hint)
                iconCustom = it.getDrawable(R.styleable.CustomEditTextView_etv_icon_custom_src)
                background = it.getDrawable(R.styleable.CustomEditTextView_etv_background_custom_src)
                editTextType = it.getString(R.styleable.CustomEditTextView_etv_input_type)
                it.recycle()
            }.also {
                setupEditText()
            }
    }

    private fun setupEditText() = binding.editText.apply {
        initEditText()
        handlePasswordAction()
    }

    private fun EditText.initEditText() {
        compoundDrawablePadding = (16.dpToPx(context))
        textHint?.let { hint = it }
        backgroundCustom?.let { background = it }
        iconCustom?.let { setCompoundDrawablesRelativeWithIntrinsicBounds(it, null, null, null) }
        inputType = when(editTextType) {
            "text" -> InputType.TYPE_CLASS_TEXT
            "phone" -> InputType.TYPE_CLASS_PHONE
            "number" -> InputType.TYPE_CLASS_NUMBER
            "email" -> InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            "name" -> InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            "password" -> InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            else -> InputType.TYPE_CLASS_TEXT
        }
    }

    /*
    * 1. Get Drawable at compoundDrawables[index]:
    *   => [DRAWABLE_LEFT] = 0, [DRAWABLE_TOP] = 1, [DRAWABLE_RIGHT] = 2, [DRAWABLE_BOTTOM] = 3
    * 2. The method OnTouchListener is called twice the touch and release events.
    *   => So, my check below makes it work like OnCLickListener
    * */
    private fun EditText.handlePasswordAction() {
        if (isPasswordType()) {
            iconCustom?.let { setCompoundDrawablesRelativeWithIntrinsicBounds(it, null, hidePasswordDrawable, null) }
            setOnTouchListener { view, motionEvent ->
                if (motionEvent.rawX >= (right - compoundDrawables[2].bounds.width())) {
                    isClickHandled = !isClickHandled
                    if (!isClickHandled) {
                        view.performClick()
                        changePasswordVisibility()
                    }
                }
                false
            }
        }
    }

    private fun EditText.isPasswordType(): Boolean {
        return inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
    }

    private fun EditText.changePasswordVisibility() {
        if (transformationMethod is PasswordTransformationMethod) {
            transformationMethod = null
            iconCustom?.let { setCompoundDrawablesRelativeWithIntrinsicBounds(it, null, showPasswordDrawable, null) }
        } else {
            transformationMethod = PasswordTransformationMethod.getInstance()
            iconCustom?.let { setCompoundDrawablesRelativeWithIntrinsicBounds(it, null, hidePasswordDrawable, null) }
        }
    }

    companion object {

        @BindingAdapter(value = ["app:textValue"])
        @JvmStatic
        fun setTextValue(view: CustomEditTextView, textValue: MutableLiveData<String>) {
            textValue.value?.let {
                if(view.editText.text.toString() != it) {
                    view.editText.setText(it)
                }
            }
        }

        @InverseBindingAdapter(attribute = "app:textValue")
        @JvmStatic
        fun getTextValue(view: CustomEditTextView): String {
            return view.editText.text.toString().trim()
        }

        @BindingAdapter(value = ["app:textValueAttrChanged"])
        @JvmStatic
        fun setTextWatcher(view: CustomEditTextView, textAttrChanged: InverseBindingListener?) {
            val newValue = object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    textAttrChanged?.onChange()
                }
                override fun afterTextChanged(p0: Editable?) {}
            }
            val oldValue: TextWatcher? = ListenerUtil.trackListener(view.editText, newValue, R.id.edit_text)
            oldValue?.let { view.editText.removeTextChangedListener(it) }
            view.editText.addTextChangedListener(newValue)
        }
    }
}