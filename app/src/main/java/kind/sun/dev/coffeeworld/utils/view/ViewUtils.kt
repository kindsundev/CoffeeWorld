package kind.sun.dev.coffeeworld.utils.view

import android.graphics.BitmapFactory
import android.text.InputType
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import kind.sun.dev.coffeeworld.R

@BindingAdapter("app:passwordInputType")
fun setPasswordInputType(editText: EditText, showPassword: Boolean) {
    if (showPassword) {
        editText.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
    } else {
        editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }
    editText.setSelection(editText.text.length)
}

@BindingAdapter("app:imageUrl")
fun loadImageByGlide(imageView: ImageView, imageUrl: String?) {
    if (imageUrl != null) {
        Glide.with(imageView.context)
            .load(imageUrl)
            .placeholder(R.drawable.img_coffee_loading)
            .centerCrop()
            .into(imageView)
    } else {
        imageView.setImageResource(R.drawable.img_item_cafe)
    }
}

@BindingAdapter("app:imageFromByteArrayToBitmap")
fun loadImageBitmapFromByteArray(imageView: ImageView, byteArray: ByteArray?) {
    if (byteArray != null) {
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        imageView.setImageBitmap(bitmap)
    }
}