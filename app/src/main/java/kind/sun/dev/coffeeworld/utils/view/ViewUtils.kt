package kind.sun.dev.coffeeworld.utils.view

import android.content.Context
import android.graphics.BitmapFactory
import android.text.InputType
import android.view.Gravity
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

@BindingAdapter("app:textResId")
fun setTextResId(view: TextView, resId: Int?) {
    resId?.let {
        view.text = view.resources.getString(it)
    }
}

@BindingAdapter("app:imgResId")
fun setImageResId(view: ImageView, resId: Int?) {
    resId?.let {
        view.setImageResource(it)
    }
}

fun showAlertDialog(
    context: Context, title: String, message: String,
    onConfirmListener: () -> Unit
) {
    val dialog = MaterialAlertDialogBuilder(context, R.style.confirm_alert_dialog).apply {
        setTitle(title)
        setMessage(message)
        setCancelable(false)
        setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        setPositiveButton(R.string.ok) { dialog, _ ->
            onConfirmListener.invoke()
            dialog.dismiss()
        }
    }.create()
    dialog.window?.apply {
        setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        setGravity(Gravity.CENTER)
    }
    dialog.show()
}