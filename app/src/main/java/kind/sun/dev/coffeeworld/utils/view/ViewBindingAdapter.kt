package kind.sun.dev.coffeeworld.utils.view

import android.text.InputType
import android.widget.EditText
import androidx.databinding.BindingAdapter

@BindingAdapter("app:passwordInputType")
fun setPasswordInputType(editText: EditText, showPassword: Boolean) {
    if(showPassword) {
        editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
    } else {
        editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }
    editText.setSelection(editText.text.length)
}