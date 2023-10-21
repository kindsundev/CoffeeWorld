package kind.sun.dev.coffeeworld.utils.helper.view

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kind.sun.dev.coffeeworld.R
import java.io.Serializable

inline fun <reified T: Serializable> Bundle.getSerializableSafe(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
    else -> @Suppress("DEPRECATION")getSerializable(key) as T?
}

inline fun <reified T : Serializable> Intent.getSerializableSafe(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION")getSerializableExtra(key) as T?
}

inline fun <reified T: Parcelable> Bundle.getParcelableSafe(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION")getParcelable(key) as T?
}

inline fun <reified T: Parcelable> Intent.getParcelableSafe(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION")getParcelableExtra(key) as T?
}

inline fun checkSDKTiramisu(onSDKTiramisu: () -> Unit, onNotTiramisu: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        onSDKTiramisu()
    } else {
        onNotTiramisu()
    }
}

inline fun checkPermission (
    context: Context, permission: String, onGranted: () -> Unit, onDenied: () -> Unit)
{
    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
        onGranted()
    } else {
        onDenied()
    }
}

fun showAlertDialog(
    context: Context, resTitleId: Int, resMessageId: Int,
    onConfirmListener: () -> Unit
) {
    MaterialAlertDialogBuilder(context, R.style.confirm_alert_dialog).apply {
        setTitle(context.getString(resTitleId))
        setMessage(context.getString(resMessageId))
        setCancelable(false)
        setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
        setPositiveButton(R.string.ok) { dialog, _ ->
            onConfirmListener.invoke()
            dialog.dismiss()
        }
    }.create().also {
        it.window?.apply {
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.CENTER)
        }
    }.show()
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun Int.dpToPx(context: Context): Int {
    val density = context.resources.displayMetrics.density
    return (this * density + 0.5f).toInt()
}

fun showSnackbarMessage(
    context: Context,
    root: CoordinatorLayout,
    resMessageId: Int,
    gravity: Int = Gravity.TOP or Gravity.CENTER_HORIZONTAL,
    duration: Int = Snackbar.LENGTH_SHORT,
    animMode: Int = BaseTransientBottomBar.ANIMATION_MODE_FADE,
    margin: Int = 16
) {
    Snackbar.make(root, context.getString(resMessageId), duration).apply {
        view.apply {
            findViewById<TextView>(com.google.android.material.R.id.snackbar_text)?.let {
                it.textAlignment = View.TEXT_ALIGNMENT_CENTER
            }
            layoutParams = (view.layoutParams as CoordinatorLayout.LayoutParams).apply {
                this.gravity = gravity
                val marginPx = margin.dpToPx(context)
                setMargins(marginPx, marginPx, marginPx, marginPx)
            }
            animationMode = animMode
        }
    }.show()
}

fun TextView.showErrorMessage(message: String) {
    this.also {
        visibility = View.VISIBLE
        text = message
    }
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}